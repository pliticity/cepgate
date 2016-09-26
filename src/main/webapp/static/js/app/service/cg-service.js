function CgService(serv, resource){

    serv.getAll = function (params) {
        return resource.query(params);
    };

    serv.getOne = function (id) {
        return resource.get({id:id});
    };

    serv.save = function (form, data,succ) {
        form.$submitted = true;
        if (form.$valid) {
            resource.save({},data,succ);
            form.$submitted = false;
            return true;
        }else{
            return false;
        }
    };

    serv.getNew = function () {
        return resource.get({new:true});
    };

};