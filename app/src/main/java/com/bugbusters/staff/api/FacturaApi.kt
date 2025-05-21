package com.bugbusters.staff.api;

import com.bugbusters.staff.dto.FacturaDTO;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FacturaApi {
    @POST("facturas/generar/{pedidoId}")
    Call<FacturaDTO> generarFactura(@Path("pedidoId") Long pedidoId);
}
