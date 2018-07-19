<template>
  <div>
    <b-container>
      <b-row>
        <b-col>
          <h1>{{ createMode ? 'Create Person' : 'Update Person' }}</h1>
        </b-col>
      </b-row>
      <b-form @submit="onSubmit" @reset="onReset">
        <b-row>
          <b-col>
            <b-form-group id="given-name-group"
                          label="Given Name"
                          label-for="given-name-input">
              <b-form-input id="given-name-input"
                            type="text"
                            v-model="person.givenName"
                            required
                            placeholder="Enter Given Name...">
              </b-form-input>
            </b-form-group>
            <b-form-group id="family-name-group"
                          label="Family Name"
                          label-for="family-name-input">
              <b-form-input id="family-name-input"
                            type="text"
                            v-model="person.familyName"
                            required
                            placeholder="Enter Family Name...">
              </b-form-input>
            </b-form-group>
            <b-form-group id="birth-date-group"
                          label="Birth Date"
                          label-for="birth-date-input">
              <b-form-input id="birth-date-input"
                            type="date"
                            v-model="person.birthDate"
                            required
                            placeholder="Enter Birth Date...">
              </b-form-input>
            </b-form-group>

            <div class="w-100">
              <b-btn class="float-left mx-1" type="reset" variant="danger">Reset Form</b-btn>
              <b-btn class="float-right mx-1" type="submit" variant="primary">Save</b-btn>
              <b-btn class="float-right mx-1" @click="back"
                     variant="default">Cancel
              </b-btn>
            </div>
          </b-col>
        </b-row>
      </b-form>
    </b-container>
  </div>
</template>

<script>
  export default {
    name: "Person",
    data() {
      this.fetchData(this.$route.params.id)
      return {
        person: {
          givenName: "",
          familyName: "",
          birthDate: "2000-01-01",
          contactInfos: [
            {
              type: "MOBILE",
              data: ""
            },
            {
              type: "FACEBOOK",
              data: ""
            },
          ]
        },
        createMode: true
      }
    },
    beforeRouteUpdate(to, from, next) {
      this.fetchData(to.params.id)
      next()
    },
    methods: {
      onSubmit() {

      },
      onReset() {

      },
      back() {
        this.$router.push({path: '/persons'})
      },
      fetchData(id) {
        if (id != null && id !== "new") {
          this.$http.get('/persons/' + id).then((response) => {
            this.person = response.data
            this.createMode = false
          });
        } else {
          this.person = {
            givenName: "",
            familyName: "",
            birthDate: "2000-01-01",
            contactInfos: [
              {
                type: "MOBILE",
                data: ""
              },
              {
                type: "FACEBOOK",
                data: ""
              },
            ]
          }
          this.createMode = true
        }
      },
      labelFor(contactInfo) {
        switch (contactInfo.type) {
          case "":
          case "CUSTOM":
            return contactInfo.customLabel
          case "STACKOVERFLOW":
            return "StackOverflow"
          case "GITHUB":
            return "GitHub"
          case "MOBILE":
          case "HOME":
          case "WORK":
          case "FAX":
          case "TWITTER":
          case "FACEBOOK":
          case "BITBUCKET":
          default:
            return contactInfo.type.substr(0, 1) + contactInfo.type.substr(1).toLowerCase()
        }
      },
      iconNameFor(contactInfo) {
        switch (contactInfo.type) {
          case "":
          case "CUSTOM":
            return "tag"
          case "STACKOVERFLOW":
            return "stack-overflow"
          case "GITHUB":
            return "github"
          case "MOBILE":
            return "mobile"
          case "HOME":
          case "WORK":
            return "phone"
          case "FAX":
            return "fax"
          case "TWITTER":
            return "twitter"
          case "FACEBOOK":
            return "facebook"
          case "BITBUCKET":
            return "bitbucket"
          default:
            return contactInfo.type.substr(0, 1) + contactInfo.type.substr(1).toLowerCase()
        }
      }
    }
  }
</script>

<style scoped>

</style>
