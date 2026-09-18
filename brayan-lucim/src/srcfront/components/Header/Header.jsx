import React from "react";
import "./Header.css";

const Header = (params) => {
    const {titulo} = params;

    return(
        <div className="header">
            <h2>{titulo}</h2>
        </div>
    );
}

export default Header;