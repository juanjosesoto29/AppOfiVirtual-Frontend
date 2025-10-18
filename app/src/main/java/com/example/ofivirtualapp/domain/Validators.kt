package com.example.ofivirtualapp.domain

import android.util.Patterns

//validaciones para el nombre:  no este vacio, solo letras
fun validateNameLettersOnly(nombre: String): String?{
    if(nombre.isBlank()) return "El nombre es obligatorio"
    val regex = Regex("^[A-Za-zÁÉÍÓÚÑáéíóúñ ]+$")
    return if(!regex.matches(nombre)) "Solo se aceptan letras y espacios" else null
}

//validaciones del correo: formato y no este vacio
fun validateEmail(email: String): String?{
    if(email.isBlank()) return "El correo es obligatorio"
    val ok = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    return if(!ok) "Formato de correo Inválido" else null
}

//validacion de teléfono: no vacio, longitud, solo numeros
fun validatePhoneDigitsOnly(phone:String): String? {
    if(phone.isBlank()) return "El teléfono es obligatorio"
    if(!phone.all { it.isDigit() }) return "Solo se aceptan números"
    if(phone.length !in 8 .. 9) return "Debe contener entre 8 y 9 digitos"
    return null
}

//validaciones para la seguridad de la contraseña
fun validateStrongPass(pass: String): String? {
    if (pass.isBlank()) return "Debes escribir tu contraseña"
    if (pass.length < 8) return "Debe tener una longitud de más de 7 caracteres"
    if (!pass.any { it.isUpperCase() }) return "Debe contener al menos una mayúscula"
    if (!pass.any { it.isDigit() }) return "Debe contener al menos un número"
    if (!pass.any { it.isLowerCase() }) return "Debe contener al menos una minúscula"

    // --- 🔹 LÓGICA CORREGIDA Y ROBUSTA PARA CARACTERES ESPECIALES 🔹 ---
    // Esto verifica que haya al menos un carácter que NO sea ni letra ni dígito.
    if (pass.all { it.isLetterOrDigit() }) return "Debe contener al menos un caracter especial"
    // --------------------------------------------------------------------

    if (pass.contains(' ')) return "No puede contener espacios en blanco"
    return null
}


//validar que las claves coincidan
fun validateConfirm(pass:String, confirm: String): String?{
    if(confirm.isBlank()) return "Debe confirmar su contraseña"
    return if(pass != confirm) "Las contraseñas no son iguales" else null
}