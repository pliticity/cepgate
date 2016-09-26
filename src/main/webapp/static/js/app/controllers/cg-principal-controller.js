function CgPrincipalController(ctrl,principalService, domainId){

    ctrl.principals = [];

    ctrl.getPrincipals = function () {
        if(domainId!=null){
            ctrl.principals = principalService.getPrincipalsInGivenDomain(domainId);
        }else{
            ctrl.principals = principalService.getPrincipalsInDomain();
        }
    };

    ctrl.getPrincipals();

};