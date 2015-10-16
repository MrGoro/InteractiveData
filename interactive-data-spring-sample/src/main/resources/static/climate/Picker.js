function Picker(defaultMin, defaultMax, defaultStart, defaultEnd, defaultGranularity) {

    var min = defaultMin;
    var max = defaultMax;
    var start = defaultStart;
    var end = defaultEnd;
    var selectedStart = null;
    var selectedEnd = null;
    var granularity = defaultGranularity;

    this.getSelectedStart = function() {
        if(selectedStart) return selectedStart;
        return start;
    };

    this.getSelectedEnd = function() {
        if(selectedEnd) return selectedEnd;
        return end;
    };

    this.resetSelected = function() {
        selectedStart = null;
        selectedEnd = null;
    };

    this.getStart = function() {
        return start;
    };

    this.getEnd = function() {
        return end;
    };

    this.getGranularity = function() {
        return granularity;
    };

    this.getFineTicks = function() {
        var timeLevel = this.getFineTimeLevel();
        return timeLevel.range;
    };

    this.getCoarseTicks = function() {
        var timeLevel = this.getTimeLevel();
        return timeLevel.range;
    };

    this.getFineTicksCount = function() {
        if(granularity == Granularity.MONTH) return 16;
        if(granularity == Granularity.DAY) return 12;
        return 6;
    };

    this.getTimeLevel = function() {
        if(granularity == Granularity.MONTH) return d3.time.month;
        if(granularity == Granularity.DAY) return d3.time.day;
        return d3.time.year;
    };

    this.getFineTimeLevel = function() {
        if(granularity == Granularity.MONTH) return d3.time.day;
        if(granularity == Granularity.DAY) return d3.time.hour;
        return d3.time.month;
    };

    this.getTimeStep = function(checkGranularity) {
        if(!checkGranularity) {
            checkGranularity = granularity;
        }
        if(checkGranularity == Granularity.MONTH) return "years";
        if(checkGranularity == Granularity.DAY) return "month";
        return "years";
    };

    this.getTimeAmount = function(checkGranularity) {
        if(!checkGranularity) {
            checkGranularity = granularity;
        }
        if(checkGranularity == Granularity.MONTH) return 1;
        if(checkGranularity == Granularity.DAY) return 1;
        return 10;
    };

    this.next = function() {
        start.add(this.getTimeAmount(), this.getTimeStep());
        end.add(this.getTimeAmount(), this.getTimeStep());
        if(end.isAfter(max)) {
            end = max.clone();
            start = max.clone().subtract(this.getTimeAmount(), this.getTimeStep());
        }
        this.renderBrush();
        this.renderChart();
    };

    this.previous = function() {
        start.subtract(this.getTimeAmount(), this.getTimeStep());
        end.subtract(this.getTimeAmount(), this.getTimeStep());
        if(start.isBefore(min)) {
            start = min.clone();
            end = min.clone().add(this.getTimeAmount(), this.getTimeStep());
        }
        this.renderBrush();
        this.renderChart();
    };

    this.getLowerGranularity = function() {
        if(granularity == Granularity.YEAR) return Granularity.MONTH;
        return Granularity.DAY;
    };

    this.getHigherGranularity = function() {
        if(granularity == Granularity.DAY) return Granularity.MONTH;
        return Granularity.YEAR;
    };

    this.selected = function(newStart, newEnd) {
        // drill down if lower range is selected
        selectedStart = newStart.clone();
        selectedEnd = newEnd.clone();
        if(newStart.isSame(start) && newEnd.isSame(end)) {
            this.resetSelected();
            granularity = this.getHigherGranularity();
            start = newStart.clone();
            end = newEnd.clone().add(this.getTimeAmount(), this.getTimeStep());
            if(end.isAfter(max)) {
                end = max.clone();
                start = max.clone().subtract(this.getTimeAmount(), this.getTimeStep());
            }
            this.renderBrush();
        } else if(granularity != Granularity.DAY) {
            var lowerGranularity = this.getLowerGranularity();
            var added = newStart.clone().add(this.getTimeAmount(lowerGranularity), this.getTimeStep(lowerGranularity));
            if (added.isSame(newEnd)) {
                start = newStart;
                end = newEnd;
                granularity = lowerGranularity;
                this.renderBrush();
            }
        }
        this.renderChart();
    };

    this.renderBrush = function() {
        this.resetSelected();
        showBrush(start, end, this.getCoarseTicks(), this.getFineTicks(), this.getFineTicksCount());
    };

    this.renderChart = function() {
        chart.loadData();
    };

    this.reset = function() {
        granularity = defaultGranularity;
        start = defaultStart;
        end = defaultEnd;
        this.renderBrush();
    };

    this.renderBrush();
}