(function () {

    var document = angular.module('document');

    document.factory('Doc', ['$resource',
        function ($resource) {
            var parseResponseDates = function(response) {
                var data = response.data, key, value;
                for (key in data) {
                    if (!data.hasOwnProperty(key) && // don't parse prototype or non-string props
                        toString.call(data[key]) !== '[object String]') continue;
                    value = Date.parse(data[key].creationDate); // try to parse to date
                    if (value !== NaN) response.resource[key].creationDate = value;
                }
                return response;
            }

            $resource('/document/:id', {}, {'query': {'url': '/document/query', 'isArray': true,interceptor: {response: parseResponseDates}}});
        }]);

    document.service('docService', ['Doc', function (Doc) {

        CgService(this,Doc);

    }]);

})();