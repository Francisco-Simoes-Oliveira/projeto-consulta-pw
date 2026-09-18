import App from "../App";
import BaseService from "./BaseService";

class HomeService extends BaseService{
    constructor(){
        super("/api/home")
    }

    async  getAgora(){
        const response = this.list();
        return response;
    }
}

export default HomeService;