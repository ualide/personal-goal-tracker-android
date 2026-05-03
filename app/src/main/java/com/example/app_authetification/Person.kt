package com.example.app_authetification

class Person(
    private var name: String,
    private var email: String,
    private var password: String,
    private var status: String = "Active"
) {
    companion object {
        private var nextId = 1
            get() {
                field += 1
                return field
            }
    }

    private var id = nextId

    fun getId(): Int {
        return id
    }

    fun getName(): String {
        return name
    }

    fun getEmail(): String {
        return email
    }

    fun getPassword(): String {
        return password
    }

    fun getStatus(): String {
        return status
    }
    fun setId(id: Int) {
        this.id = id
    }

    fun setName(name: String) {
        this.name = name
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun setStatus(status: String) {
        this.status = status
    }
}
