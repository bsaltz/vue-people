<template>
  <div>
    <b-container>
      <b-row>
        <b-col>
          <h1>Vue People</h1>
          <p>
            Your current address book.
          </p>
        </b-col>
      </b-row>
      <b-row>
        <b-col sm="8">
          <b-pagination align="center" v-model="currentPage" :per-page="perPage"
                        :total-rows="totalRows"></b-pagination>
        </b-col>
        <b-col>
          <router-link class="btn btn-success" to="/persons/new">
            New Person
          </router-link>
        </b-col>
      </b-row>
      <b-row align-v="center" class="my-1">
        <b-col>
          <b-form-checkbox v-model="stacked">Stacked Display</b-form-checkbox>
        </b-col>
        <b-col>
          <div align="center">
            Displaying {{ totalRows === 0 ? '0' : (currentPage - 1) * perPage + 1 }}
            - {{ (currentPage * perPage < totalRows) ? currentPage * perPage : totalRows }}
            of {{ totalRows }}
          </div>
        </b-col>
        <b-col>
          <b-form-group horizontal label="Rows" class="float-center mb-0">
            <b-form-select :options="pageOptions" v-model="perPage"></b-form-select>
          </b-form-group>
        </b-col>
      </b-row>
      <b-row>
        <b-col>
          <b-table hover :stacked="stacked" :items="fetchData" :fields="fields"
                   :current-page="currentPage" :per-page="perPage"
                   :total-rows="totalRows" sort-by="name" ref="peopleTable">
            <template slot="name" slot-scope="data">
              {{data.item.familyName}}, {{data.item.givenName}}
            </template>
            <template slot="actions" slot-scope="data">
              <router-link
                :to="'/persons/' + data.item._links.self.href.substr(data.item._links.self.href.lastIndexOf('/') + 1)"
                class="text-success">
                <font-awesome-icon icon="edit"/>
              </router-link>
              |
              <a href="javascript:void(0)" @click="showDelete(data.item)" class="text-danger">
                <font-awesome-icon icon="trash"/>
              </a>
              |
              <a href="javascript:void(0)" @click="data.toggleDetails()" class="text-primary">
                <font-awesome-icon icon="address-book"/>
                {{ data.item.contactInfos.length }}
              </a>
            </template>
            <template slot="row-details" slot-scope="data">
              <b-card>
                <b-row v-if="data.item.contactInfos.length > 0" class="mb-2"
                       v-for="contactInfo in data.item.contactInfos">
                  <b-col sm="3" class="text-sm-right">
                    <b>{{labelFor(contactInfo)}}</b>
                    <font-awesome-icon :icon="iconFor(contactInfo)"/>
                  </b-col>
                  <b-col v-if="linkPrefixes[contactInfo.type] != null">
                    <a :href="linkPrefixes[contactInfo.type] + contactInfo.data">
                      {{ contactInfo.data }}
                    </a>
                  </b-col>
                  <b-col v-else>
                    {{ contactInfo.data }}
                  </b-col>
                </b-row>
                <b-row v-if="data.item.contactInfos.length === 0" class="mb-2">
                  <b-col sm="3" class="text-sm-right"><b>No contact info.</b></b-col>
                </b-row>
              </b-card>
            </template>
          </b-table>
        </b-col>
      </b-row>
      <b-row>
        <b-col>
          <b-pagination align="center" v-model="currentPage" :per-page="perPage"
                        :total-rows="totalRows"></b-pagination>
        </b-col>
      </b-row>
    </b-container>

    <b-modal ref="personDelete" centered
             :title="'Delete ' + toDelete.givenName + ' ' + toDelete.familyName + '?'">
      <p>
        You're about to delete {{toDelete.givenName}} {{toDelete.familyName}}.
      </p>
      <div slot="modal-footer" class="w-100">
        <p class="float-left"><b>Continue?</b></p>
        <b-btn class="float-right mx-1" @click="doDelete()" variant="danger">Yes</b-btn>
        <b-btn class="float-right mx-1" @click="hideDelete()" variant="default">Cancel</b-btn>
      </div>
    </b-modal>
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
    name: "Persons",
    data() {
      return {
        stacked: false,
        currentPage: 1,
        perPage: 10,
        pageOptions: [5, 10, 20, 50],
        totalRows: 0,
        fields: [
          {
            key: 'name',
            sortable: true
          },
          {
            key: 'birthDate',
            sortable: true
          },
          'actions'
        ],
        toDelete: {
          givenName: '',
          familyName: '',
          id: ''
        },
        labels: {
          "MOBILE": "Mobile",
          "HOME": "Home",
          "WORK": "Work",
          "FAX": "Fax",
          "TWITTER": "Twitter",
          "FACEBOOK": "Facebook",
          "STACKOVERFLOW": "StackOverflow",
          "GITHUB": "GitHub",
          "BITBUCKET": "Bitbucket"
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
          "CUSTOM": "tag"
        },
        linkPrefixes: {
          "MOBILE": "tel:+1",
          "HOME": "tel:+1",
          "WORK": "tel:+1",
          "FAX": "tel:+1",
          "FACEBOOK": "https://facebook.com/",
          "TWITTER": "https://twitter.com/",
          "GITHUB": "https://github.com/",
          "BITBUCKET": "https://bitbucket.org/",
          "STACKOVERFLOW": "https://stackoverflow.com/cv/"
        },
        apiData: {
          _embedded: {
            persons: []
          },
          _links: {},
          page: {
            size: 0,
            totalElements: 0,
            totalPages: 0,
            number: 0
          }
        }
      }
    },
    methods: {
      showDelete(person) {
        this.toDelete.givenName = person.givenName
        this.toDelete.familyName = person.familyName
        this.toDelete.id = person._links.self.href.substr(person._links.self.href.lastIndexOf('/') + 1)
        this.$refs.personDelete.show()
      },
      hideDelete() {
        this.$refs.personDelete.hide()
      },
      doDelete() {
        this.$http.delete("/persons/" + this.toDelete.id).finally(() => {
          this.$refs.personDelete.hide()
          this.$refs.peopleTable.refresh()
        })
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
      },
      fetchData(ctx, callback) {
        let page = ctx.currentPage - 1
        let size = ctx.perPage
        let sort = []
        if (ctx.sortBy != null) {
          let sortDir = ctx.sortDesc ? ',desc' : ''
          if (ctx.sortBy === "name") {
            sort.push("familyName" + sortDir)
            sort.push("givenName" + sortDir)
          } else {
            sort.push(ctx.sortBy + sortDir)
          }
        }
        let url = "/persons"
        url += "?page=" + page
        url += "&size=" + size
        for (let i in sort) {
          url += "&sort=" + sort[i]
        }
        this.$http.get(url).then((response) => {
          this.apiData = response.data
          this.totalRows = this.apiData.page.totalElements
          callback(this.apiData._embedded.persons)
        }).catch((error) => {
          console.error(error.toString())
          this.apiData = {
            _embedded: {
              persons: []
            },
            _links: {},
            page: {
              size: 0,
              totalElements: 0,
              totalPages: 0,
              number: 0
            }
          }
          this.totalRows = 0
          callback([])
        })
      }
    }
  }
</script>

<style scoped>

</style>
