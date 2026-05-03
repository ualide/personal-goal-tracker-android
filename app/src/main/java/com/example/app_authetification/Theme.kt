package com.example.app_authetification

class Theme(private var titre: String) {
    companion object {
        private var nextId = 3 // id starts at 3 and is incremented after each theme creation
        var id = nextId++
            get() {
                field++
                return field
            }
    }

    private var themeId = id

    fun getId(): Int {
        return themeId
    }

    fun getTitre(): String {
        return titre
    }

    fun setTitre(titre: String) {
        this.titre = titre
    }

    fun setId(id: Int) {
        this.themeId = id
    }
}




