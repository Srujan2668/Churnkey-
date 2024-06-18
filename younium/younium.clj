(config
    (text-field
        :name         "clientId"
        :label        "Enter clientId"
        :placeholder  ""
        :required     true
    )

    (password-field
        :name         "clientSecret"
        :label        "Enter clientSecret"
        :placeholder  "Enter Api Key"
        :required     true
    )
    (text-field
        :name "subDomain"
        :label "Domain name"
        :placeholder "Sub-domain name"
    )   


   

    (oauth2/refresh-token-with-client-credentials
        (access-token
            (source
                (http/post
                    :url "api.younium.com/auth/token"
                    (body-params
                        "client_id" "{clientId}"
                        "client_secret" "{clientSecret}"
                    )
                )
            )

            (fields
                access-token :<= "access_token"
                token-type :<= "token_type"
                refresh-token :< "refresh_token"
                scope 
                realm-id :<= "realmId"
                expires-in :<= "expires_in"
            )
        )
       
        )
 )



(default-source (http/get :base-url "https://{subDomain}.younium.com/Accounts"
        (header-params 
                    "Authorization" "Bearer [JWT token]"
                    "Accept" "application/json"
                    "api-version"  2.1
        ))
            (paging/offset  :offset-query-param-initial-value
                            :offset-query-param-name
                            :limit 
                            :limit-query-param-name "limit"
            )
            (auth/oauth2)
        (error-handler
            (when :status 404 :message "not found")
            (when :status 401 :action refresh)
            (when :status 403 :message "Forbidden")
            (when :status 400 :message "Bad Request")
        )
            
)

(entity ACCOUNTS
        "Keep track of and act on your open disputes (also known as chargebacks) to submit the best possible response"
        (api-docs-url "https://developer.younium.com/api-details#api=Production_API2-1&operation=Get-Accounts")
        (source (http/get : url "/Accounts"))
        


)