if ( !this.JSONDATA) {
    this.JSONDATA = {};
}

(function () {

    if (typeof JSONDATA.formatJsonData !== 'function') {
        JSONDATA.formatJsonData = function (jsonVal) {
            try {
                var result = jsl.parser.parse(jsonVal);
                if (result) {
                    return JSON.stringify(JSON.parse(jsonVal), null, "    ");
                }
            } catch (parseException) {
                alert("data error! Please enter a JSON format string.");
                return '';
            }
        };
    }
    if (typeof JSONDATA.compressJsonData !== 'function') {
        JSONDATA.compressJsonData = function (jsonVal) {
            try {
                var result = jsl.parser.parse(jsonVal);
                if (result) {
                    return JSON.stringify(JSON.parse(jsonVal), null, "");
                }
            } catch (parseException) {
                alert("data error! Please enter a JSON format string.");
                return "";
            }
        };
    }
}());