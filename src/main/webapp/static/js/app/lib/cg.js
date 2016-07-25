String.prototype.format = function() {
    var args = arguments;

    return this.replace(/\{(\d+)\}/g, function() {
        return args[arguments[1]];
    });
};

Array.prototype.containsObject = function (obj) {
    var contains = false;
    for (var i = 0; i < this.length; i++) {
        if (this[i] == obj) {
            contains = true;
        }
    }
    return contains;
};