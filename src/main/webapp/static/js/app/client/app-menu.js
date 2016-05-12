(function(){
	'use strict';  
	var documentApp = angular.module('document');
	documentApp.controller('MenuController', ['$http', '$scope', '$aside', function($http, $scope, $aside) {
		 var menu = this;
		 menu.isAuthenticated = false;
		 $http.get('/signup/authenticated').success(function(response){
			 if(response.result == "true"){
				 menu.isAuthenticated = true;
			 } 
		 }); 
		 
		 $scope.openAside = function(position) {
           $aside.open({
             templateUrl: 'aside.html',
             placement: position,
             backdrop: true,
             controller: function($scope, $uibModalInstance) {
               $scope.ok = function(e) {
            	   $uibModalInstance.close();
                 e.stopPropagation();
               };
               $scope.cancel = function(e) {
            	   $uibModalInstance.dismiss();
                 e.stopPropagation();
               };
             }
           })
         }
       }]);
})();	 