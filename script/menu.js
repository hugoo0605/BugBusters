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

const modal = document.getElementById('modal');
const modalImg = document.getElementById('modal-img');
const modalName = document.getElementById('modal-name');
const modalDescription = document.getElementById('modal-description');
const closeBtn = document.getElementById('close-modal');
    
document.addEventListener("DOMContentLoaded", () => {
    fetch("http://192.168.112.95:8080/api/productos")
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

                divPlato.addEventListener('click',()=>{
                    modalImg.src = producto.imagenes;
                    modalImg.alt = producto.nombre;
                    modalName.textContent = producto.nombre;
                    modalDescription.textContent = producto.descripcion;
                    modal.classList.add('active');
                    window.scrollTo({ top: 0, behavior: 'smooth' });
                });

                closeBtn.addEventListener('click', () => {
                    modal.classList.remove('active');
                });

                modal.addEventListener('click', (e) => {
                    if (e.target === modal) {
                    modal.classList.remove('active');
                    }
                });
        
                contenedor.appendChild(divPlato);
            });
        })
    .catch(error => console.error("Error al cargar productos:", error));
});

fetch("http://localhost:8080/api/productos/categorias")
  .then(res => res.json())
  .then(categorias => {
    const contenedor = document.querySelector(".contenedor-categorias");
    contenedor.innerHTML = ""; // Limpiar antes de añadir

    categorias.forEach(categoria => {
      const div = document.createElement("div");
      div.classList.add("categoria");
      div.textContent = categoria;
      contenedor.appendChild(div);
    });
  });