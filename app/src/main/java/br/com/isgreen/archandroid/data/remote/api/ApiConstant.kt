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

        //region Profile
        const val FETCH_USER = "2.0/user"
        //endregion Profile

        //region Pull Request
        const val FETCH_PULL_REQUESTS = "2.0/pullrequests/{userUuid}"
        //endregion Pull Request

        const val FETCH_USER_REPOS = "2.0/repositories/{userUuid}"


    }

}