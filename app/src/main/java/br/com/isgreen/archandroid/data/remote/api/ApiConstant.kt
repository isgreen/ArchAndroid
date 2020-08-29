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
        const val FETCH_REPOS = "2.0/repositories"
        //endregion Repositories

        //region Profile
        const val FETCH_USER = "2.0/user"
        //endregion Profile

        //region Pull Request
        const val FETCH_PULL_REQUESTS = "2.0/pullrequests/{userUuid}"
        const val DO_PULL_REQUEST_MERGE = "2.0/repositories/{repoFullName}/pullrequests/{pullRequestId}/merge"
        const val DO_PULL_REQUEST_APPROVE = "2.0/repositories/{repoFullName}/pullrequests/{pullRequestId}/approve"
        const val DO_PULL_REQUEST_DECLINE = "2.0/repositories/{repoFullName}/pullrequests/{pullRequestId}/decline"
        const val FETCH_PULL_REQUEST_COMMITS = "2.0/repositories/{repoFullName}/pullrequests/{pullRequestId}/commits"
        const val FETCH_PULL_REQUEST_COMMENTS = "2.0/repositories/{repoFullName}/pullrequests/{pullRequestId}/comments"
        //endregion Pull Request

        const val FETCH_USER_REPOS = "2.0/repositories/{userUuid}"
    }

}