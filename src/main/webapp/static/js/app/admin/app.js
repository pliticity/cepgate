(function(){
	 var adminApp = angular.module('admin', ['ui.bootstrap', 'ngAside', 'ngFileUpload', 'angularUtils.directives.dirPagination']);
	 
	 adminApp.directive('onFinishRender', function ($timeout) {
	    return {
	        restrict: 'A',
	        link: function (scope, element, attr) {
	            if (scope.$last === true) {
	                $timeout(function () {
	                	$(".multipleitems").multiselect({includeSelectAllOption: true}); 
	                });
	            }
	        }
	    }
	});
	 
	
	 	 
	 
	 
})();
 

//Use x-www-form-urlencoded Content-Type

/**
 * The workhorse; converts an object to x-www-form-urlencoded serialization.
 * @param {Object} obj
 * @return {String}
 */ 
var param = function(obj) {
  var query = '', name, value, fullSubName, subName, subValue, innerObj, i;
    
  for(name in obj) {
    value = obj[name];
      
    if(value instanceof Array) {
      for(i=0; i<value.length; ++i) {
        subValue = value[i];
        fullSubName = name + '[' + i + ']';
        innerObj = {};
        innerObj[fullSubName] = subValue;
        query += param(innerObj) + '&';
      }
    }
    else if(value instanceof Object) {
      for(subName in value) {
        subValue = value[subName];
        fullSubName = name + '[' + subName + ']';
        innerObj = {};
        innerObj[fullSubName] = subValue;
        query += param(innerObj) + '&';
      }
    }
    else if(value !== undefined && value !== null)
      query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&';
  }
    
  return query.length ? query.substr(0, query.length - 1) : query;
};

$(".nav-pills").on("click", "a", function(e){
    e.preventDefault();
    $(this).tab('show');
  })
  .on("click", "span", function () {
      var anchor = $(this).siblings('a');
      $(anchor.attr('href')).remove();
      $(this).parent().remove();
      $(".nav-tabs li").children('a').first().click();
      $('#home').click();
  });

function renderMultiSelect(){
	  console.log("test");
}
function padDigits(number, digits) {
    return Array(Math.max(digits - String(number).length + 1, 0)).join(0) + number;
}