<template>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container-fluid">
            <a class="navbar-brand" href="/">Home</a>
            <b-form class="d-flex" v-on:submit="onSubmit" v-if="!authenticated">
                <b-form-input type="password" v-model="pwd" placeholder="Password"></b-form-input>
                <button class="btn btn-outline-success" type="submit">Login</button>
            </b-form>
            <a class="btn btn-outline-warning" role="button" href="#" v-on:click="onLogout" v-else>Logout</a>
        </div>
    </nav>
</template>

<script>
  export default {
    name: 'login-bar',
    data() {
      return {
        authenticated: false,
        pwd: "",
      }
    },
    created() {
        this.updateAuthStatus();
    },
    methods: {
        updateAuthStatus() {
            fetch('api/auth.json')
                .then(response => response.json())
                .then(data => {
                    this.authenticated = data.authenticated;
                }).catch((error)=> {
                    this.authenticated = false;
                })
        },
        onSubmit(event) {
            event.preventDefault();

            fetch('api/login.json', {
                    method: 'POST', // or 'PUT'
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({password: this.pwd}),
                })
                .then(response => response.json())
                .then(data => {
                    if (data.status === "success") {
                        this.authenticated = true;
                        this.pwd = "";
                        this.$emit("authChange");
                    } else {
                        alert(`Login error: ${data.message}`);
                    }
                    
                })
                .catch((error) => {
                    console.log("login error:", error);
                    alert("Login error. Service down?");
                });
        },
        onLogout(event) {
            event.preventDefault();
            fetch('api/logout.json')
                .then(response => response.json())
                .then(data => {
                    this.authenticated = false;
                    this.pwd = "";
                    this.$emit("authChange");
                    
                    alert("Logged out!")
                })
                .catch((error) => {
                    alert("Logout error. Hmmm...");
                });
        }
    }
  }
</script>

<style>
    
</style>
