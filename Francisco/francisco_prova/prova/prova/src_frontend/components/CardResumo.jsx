
const CardResumo = (params) =>{
    const{titulo, valor} = params
    return(
        <div>
            <h3>{titulo}</h3>
            <p>{valor}</p>
        </div>
    )
}

export default CardResumo;