package br.com.isgreen.archandroid.data.remote.api

/**
 * Created by Éverdes Soares on 09/21/2019.
 */

class ApiConstant {

    companion object {

        //region Login
        const val DO_LOGIN = "https://bitbucket.org/site/oauth2/access_token"
        //endregion Login

        //region Repositories
        const val FETCH_REPOSITORIES = "2.0/repositories"
        //endregion Repositories

        const val FETCH_USER = "2.0/user"
        const val FETCH_EVENTS = "events"//"users/{username}/received_events"

    }

}