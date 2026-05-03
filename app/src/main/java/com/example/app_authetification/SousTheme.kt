package com.example.app_authetification
class SousTheme(
    private var titre: String,
    private var description: String,
    private var idTh: Int,
    private var idUs: Int,
    private var id: Int = 0
) {
    companion object {
        private var nextId = 1
            get() {
                field += 1
                return field
            }
    }

    fun getId(): Int {
        return id
    }

    fun getTitre(): String {
        return titre
    }

    fun getDescription(): String {
        return description
    }

    fun getIdTh(): Int {
        return idTh
    }

    fun getIdUs(): Int {
        return idUs
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun setTitre(titre: String) {
        this.titre = titre
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun setIdTh(idTh: Int) {
        this.idTh = idTh
    }

    fun setIdUs(idUs: Int) {
        this.idUs = idUs
    }
}
