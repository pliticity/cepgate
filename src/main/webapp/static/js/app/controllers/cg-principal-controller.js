function CgPrincipalController(ctrl, principalService, domainId) {

    ctrl.principals = [];

    ctrl.getPrincipals = function () {
        ctrl.principals = principalService.getPrincipalsInDomain(domainId);
    };

    ctrl.getPrincipals();

};