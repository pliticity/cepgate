function CgPrincipalController(ctrl,principalService){

    ctrl.principals = [];

    ctrl.getPrincipals = function () {
        ctrl.principals = principalService.getPrincipalsInDomain();
    };

    ctrl.getPrincipals();

};