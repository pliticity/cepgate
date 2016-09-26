(function () {

    var common = angular.module('common');

    common.factory('Settings', ['$resource',
        function ($resource) {
            return $resource('/settings', {}, {get: {url: '/settings',method:'get'},save : {url : '/settings',method:'post'}});
        }]);

    common.service('settingsService', ['Settings', function (Settings) {

        var settingsService = this;

        settingsService.settings = {
            itemsPerPage: 10, update: function () {
                settingsService.update();
            }
        };

        settingsService.getCurrent = function () {
            Settings.get({},function(settings){
                if(settings!=null){
                    settingsService.settings.itemsPerPage = settings.itemsPerPage;
                }
            });

        };

        settingsService.update = function () {
            Settings.save(settingsService.settings);
        };

        settingsService.getCurrent();

    }]);

})();