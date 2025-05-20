const carrito = JSON.parse(localStorage.getItem("carrito")) || [];
const contenedor = document.getElementById("carrito-contenedor");

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

carrito.forEach(producto => {
  const divPlato = document.createElement("div");
  divPlato.className = "plato";
  divPlato.innerHTML = `
    <div class="info-plato">
      <span class="contador">${producto.cantidad}</span>
      <div class="separador"></div>
      <div class="texto-plato">
        <div class="nombre-plato">${producto.nombre}</div>
        <div class="precio">${producto.precio.toFixed(2)}€</div>
      </div>
      <img class="imagen-plato" src="${producto.imagenes}" alt="${producto.nombre}">
    </div>
    <div class="control-cantidad">
      <button class="btn-menos">−</button>
      <button class="btn-mas">+</button>
    </div>
  `;

  const contadorSpan = divPlato.querySelector(".contador");
  const btnMas = divPlato.querySelector(".btn-mas");
  const btnMenos = divPlato.querySelector(".btn-menos");

  btnMas.addEventListener("click", () => {
    añadirAlCarrito(producto);
    let cantidadActual = parseInt(contadorSpan.textContent);
    cantidadActual += 1;
    contadorSpan.textContent = cantidadActual;
  });

  btnMenos.addEventListener("click", () => {
    eliminarDelCarrito(producto);
    let cantidadActual = parseInt(contadorSpan.textContent);
    if (cantidadActual > 1) {
      cantidadActual -= 1;
      contadorSpan.textContent = cantidadActual;
    } else {
      // Eliminar del DOM si la cantidad llega a 0
      divPlato.remove();
    }
  });

  contenedor.appendChild(divPlato);
});