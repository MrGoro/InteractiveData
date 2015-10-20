var margin = {top: 20, right: 80, bottom: 30, left: 50},
    width = 1140 - margin.left - margin.right,
    height = 500 - margin.top - margin.bottom,
    heightBrush = 200 - margin.top - margin.bottom;

//var parseDate = d3.time.format("%d-%b-%y").parse;

var brush;

var x = d3.time.scale().range([0, width]),
    y = d3.scale.linear().range([height, 0]);

var x2 = d3.time.scale().range([0, width]);

var xAxis = d3.svg.axis().scale(x).orient("bottom"),
    yAxis = d3.svg.axis().scale(y).orient("left");

var color = d3.scale.category10();

var line = d3.svg.line()
    .interpolate("basis")
    .x(function(d) { return x(d.date); })
    .y(function(d) { return y(d.value); });

var svg = d3.select("#graph").append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

var svgBrush = d3.select("#graph").append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", heightBrush + margin.top + margin.bottom)
    .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

function showChart(data, chartInit) {
    color.domain(d3.keys(["Hamburg", "Munich"]));

    x.domain([
        d3.min(data, function(c) { return d3.min(c.data, function(v) { return v.date; }); }),
        d3.max(data, function(c) { return d3.max(c.data, function(v) { return v.date; }); })
    ]);
    y.domain([
        d3.min(data, function(c) { return d3.min(c.data, function(v) { return v.value; }); }),
        d3.max(data, function(c) { return d3.max(c.data, function(v) { return v.value; }); })
    ]);

    var node = svg.selectAll("g.city")
        .data(data, function(d) {
            return d.name;
        });

    var enter = node.enter().append("g")
        .attr("class", "city");

    enter.append("path")
        .attr("class", "line")
        .attr("d", function(d) {
            return line(d.data);
        })
        .style("stroke", function(d) {
            return color(d.name);
        });

    enter.append("text")
        .datum(function(d) { return {name: d.name, value: d.data[d.data.length - 1]}; })
        .attr("transform", function(d) { return "translate(" + x(d.value.date) + "," + y(d.value.value) + ")"; })
        .attr("x", 3)
        .attr("dy", ".35em")
        .text(function(d) { return d.name; });

    var remove = node.exit().remove();

    if(chartInit) {
        draw(data);
    } else {
        update(data);
    }
}

function draw(data) {
    svg.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(0," + height + ")")
        .call(xAxis);

    svg.append("g")
        .attr("class", "y axis")
        .call(yAxis)
        .append("text")
        .attr("transform", "rotate(-90)")
        .attr("y", 6)
        .attr("dy", ".71em")
        .style("text-anchor", "end")
        .text("Temperature");
}

function update(data) {
    var chart = d3.select("#graph").transition();

    chart.select(".x.axis")
        .duration(750)
        .call(xAxis);

    chart.select(".y.axis")
        .duration(750)
        .call(yAxis);

/*    chart.selectAll(".line")
        .duration(750)
        .attr("d", function(d) {
            return line(d.data);
        })*/
}

var brushInit = false;

function showBrush(start, end, coarseTicks, fineTicks, fineTicksCount) {
    x2.domain([start, end]);

    brush =  d3.svg.brush()
        .x(x2)
        .on("brushend", brushended);

    if(!brushInit) {
        drawBrush(coarseTicks, fineTicks, fineTicksCount);
        brushInit = true;
    } else {
        updateBrush(coarseTicks, fineTicks, fineTicksCount);
    }
}

var xGrid = d3.svg.axis()
    .scale(x2)
    .orient("bottom")
    .tickSize(-heightBrush)
    .tickFormat("");

var xAxisBrush = d3.svg.axis()
    .scale(x2)
    .orient("bottom");

function drawBrush(coarseTicks, fineTicks, fineTicksCount) {
    svgBrush.append("rect")
        .attr("class", "grid-background")
        .attr("width", width)
        .attr("height", heightBrush);

    svgBrush.append("g")
        .attr("class", "x grid")
        .attr("transform", "translate(0," + heightBrush + ")")
        .call(xGrid.ticks(fineTicks, fineTicksCount))
        .selectAll(".tick")
        .classed("minor", function(d) { return d.getHours(); });

    svgBrush.append("g")
        .attr("class", "x axisBrush")
        .attr("transform", "translate(0," + heightBrush + ")")
        .call(xAxisBrush.ticks(coarseTicks))
        .selectAll("text")
        .attr("x", 6)
        .style("text-anchor", null);

    var gBrush = svgBrush.append("g")
        .attr("class", "brush")
        .call(brush)
        .call(brush.event);

    gBrush.selectAll("rect")
        .attr("height", heightBrush);
}

function updateBrush(coarseTicks, fineTicks, fineTicksCount) {
    var chart = d3.select("#graph").transition();

    chart.select(".x.grid")
        .duration(750)
        .call(xGrid.ticks(fineTicks, fineTicksCount));

    chart.select(".x.axisBrush")
        .duration(750)
        .call(xAxisBrush.ticks(coarseTicks));

    chart.select(".brush")
        .duration(750)
        .call(brush.clear());
}

function brushended() {
    if (!d3.event.sourceEvent) return; // only transition after input
    var extent0 = brush.extent(),
        extent1 = extent0.map(picker.getTimeLevel().round);

    // if empty when rounded, use floor & ceil instead
    if (extent1[0] >= extent1[1]) {
        extent1[0] = picker.getTimeLevel().floor(extent0[0]);
        extent1[1] = picker.getTimeLevel().ceil(extent0[1]);
    }

    d3.select(this).transition()
        .call(brush.extent(extent1))
        .call(brush.event);

    updateBoundaries(extent1[0], extent1[1]);
}