(function () {

    var common = angular.module('common');

    common.factory('ProductTab', [function () {
            return {
                content: function (tabsId,id) {
                    var tabId = "{0}-{1}".format(tabsId, id);
                    var tab = "<div role='tabpanel' class='tab-pane' id='{0}'><ng-include ng-controller='ProductController as ctrl' src=\"'/partials/product/details/product.html'\" ng-init=\"ctrl.setProduct('{1}')\"></ng-include></div>".format(tabId,id);
                    return tab;
                }
            };
        }]);

    common.factory('QuotationTab', [function () {
        return {
            content: function (tabsId,id) {
                var tabId = "{0}-{1}".format(tabsId, id);
                var tab = "<div role='tabpanel' class='tab-pane' id='{0}'><ng-include ng-controller='QuotationController as ctrl' src=\"'/partials/quotation/details/quotation.html'\" ng-init=\"ctrl.setQuotation('{1}')\"></ng-include></div>".format(tabId,id);
                return tab;
            }
        };
    }]);

    common.service('tabService', ['$compile','ProductTab','QuotationTab', function ($compile,ProductTab,QuotationTab) {

        var tabService = this;

        tabService.getFactory = function(factory){
          if(factory == 'product'){
              return ProductTab;
          }else if(factory == 'quotation'){
              return QuotationTab;
          }
        };

        tabService.getTab = function (tabsId, objectId) {
            var tabSelector = "#tab-{0}-{1} a".format(tabsId, objectId);
            var el = angular.element(tabSelector);
            if (el.length < 1) {
                return null;
            } else {
                return el[0];
            }
        };

        tabService.openTab = function (tabsId, objectId) {
            var tabSelector = "#tab-{0}-{1} a".format(tabsId, objectId);
            $(tabSelector).tab("show");
        };

        tabService.appendToTabs = function (tabsId, link, tab, scope) {
            var tabsSelector = "#{0}".format(tabsId);
            var tabsContentSelector = "#{0}-content".format(tabsId);

            angular.element(tabsSelector).append($compile(link)(scope));
            angular.element(tabsContentSelector).append($compile(tab)(scope));
        };

        tabService.addTab = function (args,scope) {
            var tab = tabService.getTab(args.tabsId, args.id);

            if (tab == null) {
                var factory = tabService.getFactory(args.factory);

                var tabId = "{0}-{1}".format(args.tabsId, args.id);
                var link = "<li id='tab-{0}' role='presentation'><a cg-close-tab=\"{ tabsId : '".format(tabId)+args.tabsId+"' , id: '"+args.id+"' }\" href='#{0}' aria-controls='{0}' data-toggle='tab'><span>{1}</span></a></li>".format(tabId,args.name);

                var content = factory.content(args.tabsId,args.id);
                tabService.appendToTabs(args.tabsId, link, content, scope);
            }

            if (args.open == true) {
                tabService.openTab(args.tabsId, args.id);
            }
        };

    }]);

})();