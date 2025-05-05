function añadirAlCarrito(producto) {
    const carrito = JSON.parse(localStorage.getItem("carrito")) || [];
   
    const existente = carrito.find(item => item.id === producto.id);
    if (existente) {
        existente.cantidad += 1;
    } else {
        carrito.push({ ...producto, cantidad: 1 });
    }
    
    localStorage.setItem("carrito", JSON.stringify(carrito));
}
    
function eliminarDelCarrito(producto) {
    const carrito = JSON.parse(localStorage.getItem("carrito")) || [];

    const index = carrito.findIndex(item => item.id === producto.id);
    if (index !== -1) {
        carrito[index].cantidad -= 1;
        if (carrito[index].cantidad <= 0) {
            carrito.splice(index, 1);
        }
        localStorage.setItem("carrito", JSON.stringify(carrito));
    }
}
    
document.addEventListener("DOMContentLoaded", () => {
    fetch("http://localhost:8080/api/productos")
    .then(res => res.json())
    .then(productos => {
        const contenedor = document.getElementById("contenedor-menu");
        const carrito = JSON.parse(localStorage.getItem("carrito")) || [];
            productos.forEach(producto => {
                const divPlato = document.createElement("div");
                divPlato.className = "plato";
        
                const enCarrito = carrito.find(item => item.id === producto.id);
                const cantidad = enCarrito ? enCarrito.cantidad : 0;
        
                    divPlato.innerHTML = `
                    <div class="info-plato">
                        <div class="texto-plato">
                            <div class="nombre-plato">${producto.nombre}</div>
                            <div class="precio">${producto.precio.toFixed(2).replace(".", ",")}€</div>
                        </div>
                        <img class="imagen-plato" src="${producto.imagenes}" alt="${producto.nombre}">
                    </div>
                    <div class="control-cantidad">
                        <button class="btn-menos">−</button>
                        <span class="contador">${cantidad}</span>
                        <button class="btn-mas">+</button>
                    </div>
                `;
        
                const btnMas = divPlato.querySelector(".btn-mas");
                const btnMenos = divPlato.querySelector(".btn-menos");
                const contadorSpan = divPlato.querySelector(".contador");
        
                btnMas.addEventListener("click", () => {
                    let cantidad = parseInt(contadorSpan.textContent, 10) || 0;
                    contadorSpan.textContent = cantidad + 1;
                    añadirAlCarrito(producto);
                });
        
                btnMenos.addEventListener("click", () => {
                    let cantidad = parseInt(contadorSpan.textContent, 10) || 0;
                    if (cantidad > 0) {
                        contadorSpan.textContent = cantidad - 1;
                        eliminarDelCarrito(producto);
                    }
                });
        
                contenedor.appendChild(divPlato);
            });
        })
    .catch(error => console.error("Error al cargar productos:", error));
});

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
    imagen.setAttribute("src", "https://imag.bonviveur.com/croquetas-de-cocido-o-puchero.jpg");
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