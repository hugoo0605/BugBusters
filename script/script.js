function añadirComida(){
    var divContenedor = document.getElementById("contenedor");

    var divPlato = document.createElement("div");
    divPlato.setAttribute("class", "plato");

    var divInfoPlato = document.createElement("div");
    divInfoPlato.setAttribute("class", "info-plato");

    var divTextoPlato = document.createElement("div");
    divTextoPlato.setAttribute("class", "texto-plato");

    var divNombrePlato = document.createElement("div");
    divNombrePlato.setAttribute("class", "nombre-plato");
    divNombrePlato.textContent = "Tortilla Española";

    var divPrecio = document.createElement("div");
    divPrecio.setAttribute("class", "precio");
    divPrecio.textContent = "7,50€";

    var imagen = document.createElement("img");
    imagen.setAttribute("class", "imagen-plato");
    imagen.setAttribute("src", "https://recetasdecocina.elmundo.es/wp-content/uploads/2024/09/tortilla-de-patatas-con-cebolla.jpg");
    imagen.setAttribute("alt", "Tortilla Española");

    var divControlCantidad = document.createElement("div");
    divControlCantidad.setAttribute("class", "control-cantidad");

    var btnMenos = document.createElement("button");
    btnMenos.setAttribute("class", "btn-menos");
    btnMenos.textContent = "−";

    var contador = document.createElement("span");
    contador.setAttribute("class", "contador");
    contador.textContent = "0";

    var btnMas = document.createElement("button");
    btnMas.setAttribute("class", "btn-mas");
    btnMas.textContent = "+";

    divTextoPlato.appendChild(divNombrePlato);
    divTextoPlato.appendChild(divPrecio);

    divInfoPlato.appendChild(divTextoPlato);
    divInfoPlato.appendChild(imagen);

    divControlCantidad.appendChild(btnMenos);
    divControlCantidad.appendChild(contador);
    divControlCantidad.appendChild(btnMas);

    divPlato.appendChild(divInfoPlato);
    divPlato.appendChild(divControlCantidad);

    divContenedor.appendChild(divPlato);
};