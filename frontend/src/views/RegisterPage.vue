<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="register-form">
        <form @submit.prevent="submitForm">
          <div v-show="errorMessage" class="alert alert-danger failed">{{ errorMessage }}</div>
          <div class="form-group">
            <label for="email">이메일</label>
            <input type="email" class="form-control" id="email" v-model="form.email">
          </div>
          <div class="form-group">
            <label for="username">닉네임</label>
            <input type="text" class="form-control" id="username" v-model="form.username"/>
          </div>
          <div class="form-group">
            <label for="password">비밀번호</label>
            <input type="password" class="form-control" id="password" v-model="form.password"/>
          </div>
          <div class="form-group">
            <label for="password2">비밀번호 확인</label>
            <input type="password" class="form-control" id="password2" v-model="form.password2"/>
          </div>
          <button type="submit" class="btn btn-primary btn-block">
          </button>
        </form>
      </div>
    </div>
    <footer class="footer">
      <span class="copyright">joyful</span>
      <ul class="footer-link list-inline float-right"></ul>
    </footer>
  </div>
</template>

<script>
  import registrationService from '@/services/registration'
  export default {
    name: "RegisterPage",
    data: function () {
      return {
        form: {
          email: '',
          username: '',
          password: '',
          password2: '',
          errorMessage: ''
        }
      }
    },
    methods: {
      submitForm () {
        //TODO : 데이터 검증
        registrationService.register(this.form).then( () => {
          this.$router.push({name: 'LoginPage'})
        }).catch( (error) => {
          this.errorMessage = 'Falied to register member'
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  .container {
    max-width: 900px;
  }

  .register-form {
    margin-top: 50px;
    max-width: 320px;
  }

  .footer {
    width: 100%;
    line-height: 40px;
    margin-top: 50px;
  }
</style>
