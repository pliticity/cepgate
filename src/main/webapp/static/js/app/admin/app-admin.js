(function(){
	 var adminApp = angular.module('admin');
	 
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
	 
	 
	  
	 adminApp.controller('AdminController', ['$http', '$scope', 'Upload', function($http, $scope, Upload){
		 var admin = this;
		 $http.get('/adminAuth/authenticated').success(function(response){
			 if(response.result == "false"){
				 window.location = "/admin/login";
			 } 
		 });		 
	 }]);
	 
	 
})();
 
 