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
              <b-btn class="float-right mx-1" @click="back" variant="default">Cancel</b-btn>
            </div>
          </b-col>
        </b-row>
        <b-row v-if="person.contactInfos.length > 0" class="mx-2 my-3"
               v-for="(contactInfo, index) in person.contactInfos">
          <b-col sm="4" class="text-sm-right">
            <b-form-group :id="'contact-type-group-' + index"
                          :label="Type"
                          label-sr-only
                          :label-for="'contact-type-input-' + index">
              <b-input-group size="sm">
                <b-input-group-text slot="append">
                  <font-awesome-icon :icon="iconFor(contactInfo)"/>
                </b-input-group-text>
                <b-form-select :id="'contact-type-input-' + index"
                              type="text"
                              :options="types"
                              v-model="contactInfo.type"
                              required>
                </b-form-select>
              </b-input-group>
            </b-form-group>
          </b-col>
          <b-col sm="5"
                 v-if="contactInfo.type === 'MOBILE'
                       || contactInfo.type === 'HOME'
                       || contactInfo.type === 'WORK'
                       || contactInfo.type === 'FAX'">
            <b-form-group :id="'contact-info-group-' + index"
                          :label="labelFor(contactInfo)"
                          label-sr-only
                          :label-for="'contact-info-input-' + index">
              <b-input-group prepend="+1" size="sm">
                <b-form-input :id="'contact-info-input-' + index"
                              type="text"
                              v-model="contactInfo.data"
                              required>
                </b-form-input>
              </b-input-group>
            </b-form-group>
          </b-col>
          <b-col sm="5"
                 v-else-if="contactInfo.type === 'FACEBOOK'
                            || contactInfo.type === 'TWITTER'
                            || contactInfo.type === 'GITHUB'
                            || contactInfo.type === 'BITBUCKET'
                            || contactInfo.type === 'STACKOVERFLOW'">
            <b-form-group :id="'contact-info-group-' + index"
                          :label="labelFor(contactInfo)"
                          label-sr-only
                          :label-for="'contact-info-input-' + index">
              <b-input-group :prepend="domains[contactInfo.type] + '/'" size="sm">
                <b-form-input :id="'contact-info-input-' + index"
                              type="text"
                              v-model="contactInfo.data"
                              required>
                </b-form-input>
              </b-input-group>
            </b-form-group>
          </b-col>
          <b-col sm="5"
                 v-else-if="contactInfo.type === 'EMAIL'">
            <b-form-group :id="'contact-info-group-' + index"
                          :label="labelFor(contactInfo)"
                          label-sr-only
                          :label-for="'contact-info-input-' + index">
              <b-input-group size="sm">
                <b-form-input :id="'contact-info-input-' + index"
                              type="email"
                              v-model="contactInfo.data"
                              required>
                </b-form-input>
              </b-input-group>
            </b-form-group>
          </b-col>
          <b-col sm="5"
                 v-else-if="contactInfo.type === 'CUSTOM'">
            <b-form-group :id="'contact-info-group-' + index"
                          :label="labelFor(contactInfo)"
                          label-sr-only
                          :label-for="'contact-info-input-' + index">
              <b-input-group size="sm">
                <b-form-input :id="'contact-info-input-' + index"
                              type="text"
                              v-model="contactInfo.data"
                              required>
                </b-form-input>
              </b-input-group>
            </b-form-group>
          </b-col>
          <b-col>
            <div class="w-100">
              <b-button-group>
                <b-btn @click="moveUp(index)" :disabled="index === 0"
                       variant="default">
                  <font-awesome-icon icon="angle-up"/>
                </b-btn>
                <b-btn @click="moveDown(index)" :disabled="index === person.contactInfos.length - 1"
                       variant="default">
                  <font-awesome-icon icon="angle-down"/>
                </b-btn>
              </b-button-group>
              <b-button-group class="ml-1">
                <b-btn @click="deleteInfo(index)" variant="danger">
                  <font-awesome-icon icon="trash"/>
                </b-btn>
              </b-button-group>
            </div>
          </b-col>
        </b-row>
        <b-row v-if="person.contactInfos.length === 0" class="mx-2 my-3">
          <b-col sm="3" class="text-sm-right"><b>No contact info.</b></b-col>
        </b-row>
        <b-row>
          <b-col>
            <div class="w-100">
              <b-btn class="float-right"
                     @click="person.contactInfos.push({type: 'MOBILE', data: ''})"
                     variant="success">
                Add Row
              </b-btn>
            </div>
          </b-col>
        </b-row>
      </b-form>
    </b-container>
  </div>
</template>

<script>
  import {
    faStackOverflow,
    faFacebook,
    faTwitter,
    faBitbucket,
    faGithub
  } from '@fortawesome/free-brands-svg-icons'
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
              type: "EMAIL",
              data: ""
            },
            {
              type: "FACEBOOK",
              data: ""
            },
          ]
        },
        types: [
          { value: "MOBILE", text: "Mobile"},
          { value: "HOME", text: "Home"},
          { value: "WORK", text: "Work"},
          { value: "FAX", text: "Fax"},
          { value: "EMAIL", text: "Email"},
          { value: "TWITTER", text: "Twitter"},
          { value: "FACEBOOK", text: "Facebook"},
          { value: "STACKOVERFLOW", text: "StackOverflow"},
          { value: "GITHUB", text: "GitHub"},
          { value: "BITBUCKET", text: "Bitbucket"},
          { value: "CUSTOM", text: "Custom"}
        ],
        labels: {
          "MOBILE": "Mobile",
          "HOME": "Home",
          "WORK": "Work",
          "FAX": "Fax",
          "TWITTER": "Twitter",
          "FACEBOOK": "Facebook",
          "STACKOVERFLOW": "StackOverflow",
          "GITHUB": "GitHub",
          "BITBUCKET": "Bitbucket",
          "EMAIL": "Email"
        },
        icons: {
          "MOBILE": "mobile",
          "HOME": "phone",
          "WORK": "phone",
          "FAX": "fax",
          "TWITTER": faTwitter,
          "FACEBOOK": faFacebook,
          "STACKOVERFLOW": faStackOverflow,
          "GITHUB": faGithub,
          "BITBUCKET": faBitbucket,
          "EMAIL": "envelope",
          "CUSTOM": "tag"
        },
        domains: {
          "FACEBOOK": "facebook.com",
          "TWITTER": "twitter.com",
          "GITHUB": "github.com",
          "BITBUCKET": "bitbucket.org",
          "STACKOVERFLOW": "stackoverflow.com/cv"
        },
        createMode: true
      }
    },
    beforeRouteUpdate(to, from, next) {
      this.fetchData(to.params.id)
      next()
    },
    methods: {
      onSubmit(evt) {
        evt.preventDefault()
        if (this.createMode) {
          this.$http.post('/persons', this.person).then(response => {
            this.back()
          })
        } else {
          this.$http.put('/persons/' + this.$route.params.id, this.person).then(response => {
            this.back()
          })
        }
      },
      onReset(evt) {
        evt.preventDefault()
        this.fetchData(this.$route.params.id)
      },
      back() {
        this.$router.push({path: '/persons'})
      },
      moveUp(index) {
        this.swap(index, index - 1, this.person.contactInfos)
      },
      moveDown(index) {
        this.swap(index, index + 1, this.person.contactInfos)
      },
      deleteInfo(index) {
        this.person.contactInfos.splice(index, 1)
      },
      swap(i1, i2, list) {
        if (i1 < 0 || i1 >= list.length || i2 < 0 || i2 >= list.length) {
          return
        }
        let e1 = list[i1]
        list.splice(i1, 1, list[i2])
        list.splice(i2, 1, e1)
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
        if (contactInfo.type == null || contactInfo.type.length === 0) {
          return "Unknown"
        }
        let label = this.labels[contactInfo.type]
        if (label == null) {
          label = contactInfo.customLabel
        }
        if (label == null) {
          label = contactInfo.type.substr(0, 1) + contactInfo.type.substr(1).toLowerCase()
        }
        return label
      },
      iconFor(contactInfo) {
        if (contactInfo.type == null || contactInfo.type.length === 0) {
          return "tag"
        }
        let icon = this.icons[contactInfo.type]
        if (icon == null) {
          icon = "tag"
        }
        return icon
      }
    }
  }
</script>

<style scoped>

</style>
