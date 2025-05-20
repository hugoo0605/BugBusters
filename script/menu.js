const carrito = JSON.parse(localStorage.getItem("carrito")) || [];

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

function mostrarProductos(categoriaSeleccionada = "TODOS") {
  fetch("https://bugbustersspring.onrender.com/api/productos")
    .then(res => res.json())
    .then(productos => {
        const contenedor = document.getElementById("contenedor-menu");
        contenedor.innerHTML = "";

        const filtrados = categoriaSeleccionada === "TODOS"
            ? productos
            : productos.filter(p => p.categoria === categoriaSeleccionada);

        const enCarrito = carrito.find(item => item.id === producto.id);
        const cantidad = enCarrito ? enCarrito.cantidad : 0;
        
        filtrados.forEach(producto => {
            const divPlato = document.createElement("div");
            divPlato.className = "plato";
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
                
        const infoPlato = divPlato.querySelector(".info-plato");
                
        infoPlato.addEventListener('click',()=>{
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
    });
}

document.addEventListener("DOMContentLoaded", () => {
  mostrarProductos("TODOS"); // al inicio muestra todos los platos
});

const modal = document.getElementById('modal');
const modalImg = document.getElementById('modal-img');
const modalName = document.getElementById('modal-name');
const modalDescription = document.getElementById('modal-description');
const closeBtn = document.getElementById('close-modal');

fetch("https://bugbustersspring.onrender.com/api/productos/categorias")
  .then(res => res.json())
  .then(categorias => {
    const contenedorCategorias = document.querySelector(".contenedor-categorias");
    contenedorCategorias.innerHTML = "";

    // Añadir botón para mostrar todos
    const botonTodos = document.createElement("div");
    botonTodos.classList.add("categoria");
    botonTodos.textContent = "TODOS";
    botonTodos.dataset.categoria = "TODOS";
    contenedorCategorias.appendChild(botonTodos);

    categorias.forEach(categoria => {
      const div = document.createElement("div");
      div.classList.add("categoria");
      div.textContent = categoria;
      div.dataset.categoria = categoria;
      contenedorCategorias.appendChild(div);
    });

    // Añadir eventos de click a los botones una vez estén en el DOM
    document.querySelectorAll(".categoria").forEach(btn => {
      btn.addEventListener("click", () => {
        const cat = btn.dataset.categoria;
        mostrarProductos(cat);
      });
    });
  });

