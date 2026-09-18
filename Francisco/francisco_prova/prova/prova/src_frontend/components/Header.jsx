
const Header = ({titulo}) =>{
    
  
    return(
        <div>
            <h1>{titulo ?? "padrão"}</h1>
        </div>
    )
}

export default Header;