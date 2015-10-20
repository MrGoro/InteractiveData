Granularity = {
    YEAR: "YEAR",
    MONTH: "MONTH",
    DAY: "DAY"
};

function Chart(apiUrl, success, error, status, defaultPicker) {
    var picker = defaultPicker;
    var baseUrl = apiUrl;
    var data = {};
    var successCallback = success;
    var errorCallback = error;
    var statusCallback = status;

    this.setGranularity = function (granularity) {
        if(granularity == Granularity.YEAR ||
            granularity == Granularity.MONTH ||
            granularity == Granularity.DAY) {

            this.granularity = granularity;
        }
    };

    this.getGranularity = function() {
        return picker.getGranularity();
    };

    this.getStart = function() {
        return picker.getSelectedStart();
    };

    this.getEnd = function() {
        return picker.getSelectedEnd();
    };

    this.setData = function(newData) {
        data = newData;
    };

    this.getData = function() {
        return data;
    };

    this.getBaseUrl = function() {
        return baseUrl;
    };

    this.getUrl = function() {
        var url = this.getBaseUrl() + "?selected=" + this.getGranularity();
        if(this.getStart() && this.getEnd()) {
            url = url + "&start=" + this.toDateString(this.getStart()) + "&end=" + this.toDateString(this.getEnd());
        }
        return url;
    };

    this.toDateString = function(date) {
        return date.format("YYYY-MM-DDTHH:mm:ss");
    };

    this.loadData = function(init) {
        statusCallback(true);

        var path1 = this.getUrl() + '&search=1975';
        var path2 = this.getUrl() + '&search=3390';

        $.ajax({
            url: path1,
            dataType: "json"
        })
        .success(function(result1) {
                $.ajax({
                    url: path2,
                    dataType: "json"
                })
                .success(function(result2) {
                    successCallback(result1, result2, init);
                })
                .fail(function(error) {
                    errorCallback(error);
                    statusCallback(false);
                })
                .complete(function() {
                    statusCallback(false);
                });
        })
        .fail(function(error) {
            errorCallback(error);
            statusCallback(false);
        });
    };

    this.loadData(true);
}