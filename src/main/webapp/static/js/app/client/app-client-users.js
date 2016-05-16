(function(){
	angular.module('document').controller('ClientUsersController', ClientUsersController);
	ClientUsersController.$inject = ['$http', '$scope', 'Upload', '$uibModal'];
	
	function ClientUsersController($http, $scope, Upload, $uibModal){
		 var clientUsers = this;
		 clientUsers.accounts  = [];
		 clientUsers.userQ = "";
		 var messageDiv = angular.element( document.querySelector('#messageDiv'));
		 clientUsers.pageSize = 10;
		 clientUsers.user = {};
		 clientUsers.isNew = false;
		 clientUsers.isAddEditState  =false;
		 clientUsers.message = "";
		 $http.get('../../json/userType.json').success(function(response){
			 clientUsers.userType = response;
				 
			});
		 
		 $http.get('/signup/authenticated').success(function(response){
			 console.log('app-client-authenticated');
			 if(response.result == "false"){
				 window.location = "/signup/login";
			 } else {
				 clientUsers.list();
			 }
		 }); 
		 
		 clientUsers.showMessage = function(){
			 return clientUsers.message.length > 0;
		 }
		 
		 clientUsers.deleteUser = function(theAccount){
			 if(confirm('Are you sure you would like to delete this User?')){
				 $http({
					 method: "POST",
					 url: '/member/clientusers/delete/',  
					 data:   theAccount, 
				 }).success(function(response){
					  clientUsers.list();
						 
					}).error(function(response){
						if(response.error){
							messageDiv.removeClass("alert-success");
							 messageDiv.addClass("alert-danger");
							$scope.message = response.message;
						}
						 
					});
			 }
		 }
		 
		 clientUsers.edit = function(theUser){
			 theUser.passwd = "";
			 clientUsers.user = theUser;
			 clientUsers.isAddEditState = true;
			 clientUsers.isNew = false; 
		 }
		 
		 clientUsers.cancel = function(){
			 clientUsers.isAddEditState = false; 
			 clientUsers.list();
		 }
		 
		 clientUsers.create = function(){
			 clientUsers.user = {};
			 clientUsers.isAddEditState = true; 
			 clientUsers.isNew = true;
		 }

		 clientUsers.list = function(){
			 clientUsers.isAddEditState = false; 
			 $http.get('/member/clientusers/list').success(function(response){ 
				 if(!response || !response.data || response.data.length == 0 ){
					 clientUsers.accounts = []; 
					 if(response.result == "fail"){
						 messageDiv.removeClass("alert-success");
						 messageDiv.addClass("alert-danger");
						 clientUsers.message = response.message;
					 }
				 }else {
					 clientUsers.accounts = response.data; 
					 clientUsers.message = "";
				 }  
			 });
		 }

		 clientUsers.save = function(theUser){
			 if(!$scope.signupForm.$valid){
				 messageDiv.removeClass("alert-success");
				 messageDiv.addClass("alert-danger");
				 $scope.message = "Please enter all required fields";
			 }else {
				 $http({
					 method: "POST",
					 url: '/member/clientusers/save',  
					 data:  clientUsers.user,
					 
				 }).success(function(response){
					  
					  if(response.result == "fail"){
						 messageDiv.removeClass("alert-success");
						 messageDiv.addClass("alert-danger");
						 clientUsers.message = response.message;
					 }else {
						 clientUsers.list();
					 }
						 
				 }).error(function(response){
						if(response.error){
							messageDiv.removeClass("alert-success");
							 messageDiv.addClass("alert-danger");
							$scope.message = response.message;
						}
						 
					});
				 
			 }
			 
			
			 
		 }
		 
	 };
	 
})();
 

 