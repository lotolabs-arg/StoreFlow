# Guía de Contribución de StoreFlow

¡Gracias por ayudar a construir StoreFlow! Para mantener la calidad y el orden del código, seguimos estrictamente las
siguientes pautas.

## 🌿 Estrategia de Ramas (Branching)

Usamos ramas cortas y específicas. Nunca commiteamos directamente a `main`.

**Formato:** `tipo/descripcion-corta-en-ingles`

| Tipo        | Uso                                    | Ejemplo                             |
|:------------|:---------------------------------------|:------------------------------------|
| `feat/`     | Nueva funcionalidad                    | `feat/stock-decimal-support`        |
| `fix/`      | Corrección de bug                      | `fix/calculate-price-overflow`      |
| `chore/`    | Configuración/Mantenimiento            | `chore/update-pom-dependencies`     |
| `refactor/` | Mejoras de código sin cambio funcional | `refactor/extract-validation-logic` |
| `docs/`     | Documentación                          | `docs/update-readme`                |

---

## 💾 Mensajes de Commit (Conventional Commits)

Seguimos la convención [Conventional Commits](https://www.conventionalcommits.org/).

**Formato:** `tipo(ambito opcional): descripción imperativa en minúsculas`

* **Tipos permitidos:** `feat`, `fix`, `chore`, `refactor`, `docs`, `style`, `test`.
* **Reglas:**
    * Usar modo imperativo ("add" no "added").
    * **Debe escribirse en Inglés.**
    * Sin punto final.

**Ejemplos Correctos:**

* ✅ `feat(stock): add unit type enum to product`
* ✅ `fix: resolve null pointer in customer validation`
* ✅ `chore: update checkstyle rules`

---

## 🎫 Gestión de Issues

Antes de escribir código, debe existir una Issue que lo justifique.

### Etiquetas (Labels)

* **Estado:** `status:blocked` (⛔ No empezar), `status:in-progress`.
* **Tipo:** `type:bug`, `type:chore`.

### Flujo de Trabajo

1. Asígnate la Issue.
2. Mueve la Issue a la columna **"In Progress"**.
3. Crea la rama desde `main` (opcionalmente usando el número de issue): `feat/12-add-product-entity`.

---

## 🔀 Pull Requests (Solicitudes de Cambio)

1. **Título:** Debe seguir el mismo formato que los Commits (ej. `feat: Implement Unit Type Logic`).
2. **Descripción:** Usa la plantilla provista (Contexto, Cambios, Cómo probar).
3. **Revisión:**
    * El CI (Maven + Checkstyle) debe pasar en verde (Sin errores).
    * No debe haber conflictos con `main`.
4. **Merge:** Usar **"Squash and Merge"** para mantener un historial limpio en `main`.

---

## ☕ Estilo de Código (Java)

El proyecto tiene reglas estrictas validadas automáticamente por **Checkstyle**.

* **Idioma:** Todo el código (variables, métodos, javadoc) debe estar en **Inglés**.
* **Formato:** 4 espacios de indentación, `UTF-8`.
* **Reglas Clave:**
    * 🚫 Prohibido usar comentarios de línea `//`.
    * 🚫 Prohibido usar `System.out.println` (Usar Loggers).
    * 🚫 Métodos de más de 50 líneas o con alta complejidad ciclomática (>10) fallarán el build.
    * ✅ Javadoc obligatorio en métodos públicos complejos (>15 líneas).

Ejecuta `mvn clean install` localmente antes de subir cambios para verificar que cumples las reglas.
