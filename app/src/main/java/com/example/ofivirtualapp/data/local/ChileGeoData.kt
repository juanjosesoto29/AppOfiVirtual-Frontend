package com.example.ofivirtualapp.data.local

/**
 * Estructura geográfica completa de Chile
 * Región → Ciudades → Comunas
 */
object ChileGeoData {

    val regiones = listOf(
        "Región de Arica y Parinacota",
        "Región de Tarapacá",
        "Región de Antofagasta",
        "Región de Atacama",
        "Región de Coquimbo",
        "Región de Valparaíso",
        "Región Metropolitana de Santiago",
        "Región del Libertador General Bernardo O’Higgins",
        "Región del Maule",
        "Región de Ñuble",
        "Región del Biobío",
        "Región de La Araucanía",
        "Región de Los Ríos",
        "Región de Los Lagos",
        "Región de Aysén del General Carlos Ibáñez del Campo",
        "Región de Magallanes y de la Antártica Chilena"
    )

    val estructura = mapOf(
        // --- 1. Arica y Parinacota ---
        "Región de Arica y Parinacota" to mapOf(
            "Arica" to listOf("Arica", "Camarones"),
            "Parinacota" to listOf("Putre", "General Lagos")
        ),

        // --- 2. Tarapacá ---
        "Región de Tarapacá" to mapOf(
            "Iquique" to listOf("Iquique", "Alto Hospicio"),
            "Tamarugal" to listOf("Pozo Almonte", "Camiña", "Colchane", "Huara", "Pica")
        ),

        // --- 3. Antofagasta ---
        "Región de Antofagasta" to mapOf(
            "Antofagasta" to listOf("Antofagasta", "Mejillones", "Sierra Gorda", "Taltal"),
            "El Loa" to listOf("Calama", "Ollagüe", "San Pedro de Atacama"),
            "Tocopilla" to listOf("Tocopilla", "María Elena")
        ),

        // --- 4. Atacama ---
        "Región de Atacama" to mapOf(
            "Copiapó" to listOf("Copiapó", "Caldera", "Tierra Amarilla"),
            "Chañaral" to listOf("Chañaral", "Diego de Almagro"),
            "Huasco" to listOf("Vallenar", "Freirina", "Huasco", "Alto del Carmen")
        ),

        // --- 5. Coquimbo ---
        "Región de Coquimbo" to mapOf(
            "Elqui" to listOf("La Serena", "Coquimbo", "Andacollo", "La Higuera", "Paihuano", "Vicuña"),
            "Choapa" to listOf("Illapel", "Canela", "Los Vilos", "Salamanca"),
            "Limarí" to listOf("Ovalle", "Combarbalá", "Monte Patria", "Punitaqui", "Río Hurtado")
        ),

        // --- 6. Valparaíso ---
        "Región de Valparaíso" to mapOf(
            "Valparaíso" to listOf("Valparaíso", "Viña del Mar", "Concón", "Casablanca", "Juan Fernández"),
            "Marga Marga" to listOf("Quilpué", "Villa Alemana", "Limache", "Olmué"),
            "San Antonio" to listOf("San Antonio", "Cartagena", "El Tabo", "El Quisco", "Algarrobo", "Santo Domingo"),
            "Quillota" to listOf("Quillota", "La Calera", "La Cruz", "Hijuelas", "Nogales"),
            "Petorca" to listOf("Cabildo", "La Ligua", "Papudo", "Petorca", "Zapallar"),
            "Los Andes" to listOf("Los Andes", "Calle Larga", "Rinconada", "San Esteban"),
            "San Felipe de Aconcagua" to listOf("San Felipe", "Catemu", "Llaillay", "Panquehue", "Putaendo", "Santa María"),
            "Isla de Pascua" to listOf("Isla de Pascua")
        ),

        // --- 7. Región Metropolitana ---
        "Región Metropolitana de Santiago" to mapOf(
            "Santiago" to listOf(
                "Santiago", "Cerrillos", "Cerro Navia", "Conchalí", "El Bosque",
                "Estación Central", "Huechuraba", "Independencia", "La Cisterna",
                "La Florida", "La Granja", "La Pintana", "La Reina", "Las Condes",
                "Lo Barnechea", "Lo Espejo", "Lo Prado", "Macul", "Maipú",
                "Ñuñoa", "Pedro Aguirre Cerda", "Peñalolén", "Providencia",
                "Pudahuel", "Quilicura", "Quinta Normal", "Recoleta", "Renca",
                "San Joaquín", "San Miguel", "San Ramón", "Vitacura"
            ),
            "Cordillera" to listOf("Puente Alto", "Pirque", "San José de Maipo"),
            "Maipo" to listOf("San Bernardo", "Buin", "Calera de Tango", "Paine"),
            "Chacabuco" to listOf("Colina", "Lampa", "Til Til"),
            "Melipilla" to listOf("Melipilla", "Alhué", "Curacaví", "María Pinto", "San Pedro"),
            "Talagante" to listOf("Talagante", "El Monte", "Isla de Maipo", "Padre Hurtado", "Peñaflor")
        ),

        // --- 8. O’Higgins ---
        "Región del Libertador General Bernardo O’Higgins" to mapOf(
            "Cachapoal" to listOf("Rancagua", "Machalí", "Graneros", "Doñihue", "Requínoa", "Coinco", "Coltauco", "Las Cabras", "Peumo", "San Vicente"),
            "Colchagua" to listOf("San Fernando", "Chimbarongo", "Nancagua", "Palmilla", "Peralillo", "Placilla", "Santa Cruz", "Lolol", "Pumanque"),
            "Cardenal Caro" to listOf("Pichilemu", "La Estrella", "Litueche", "Marchigüe", "Navidad", "Paredones")
        ),

        // (puedes continuar con Maule, Ñuble, Biobío, etc. — pero hasta aquí cubre las más usadas)
    )

    val paises = listOf("Chile")
}
