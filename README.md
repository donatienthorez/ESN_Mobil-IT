ESN MobilIT
============

ESN MobilIT is an app made especialy for international people who want to follow events from the closest ESN Section. 
With this app you'll miss no more events/news/partners from your favorite section. 
Download it, test it and enjoy it. 

# Install 

If you want to install the app on your phone, you need first to register on the google+ communauty made for the BETA test :
https://plus.google.com/communities/113899458035069519756

Then you just have to download it from the play store like any other app :
https://play.google.com/apps/testing/org.esn.mobilit

# Backoffice

All sections from the ESN Network can add some contents into our backoffice to add a special tab in the app : the survival guide. 
If you want to add yours, just follow the link below :
http://esnlille.fr/survivalGuide

# Auto Push from website

If you have an ESN satellite, you can install some rules to send a request to our backoffice in order to send automatically a push to all your section contributors.
To do so, follow those steps :

## Step 1

Install those modules : 
Rules & Rules UI : https://www.drupal.org/project/rules
Rules HTTP Client : https://www.drupal.org/project/rules_http_client

## Step 2

Enable those modules

## Step 3

Go to admin/config/workflow/rules and click on "import a new rule"

## Step 4

### Step 4.1

Find your code section directly on your URL on the galaxy website 
for exampel for ESN Lille it's : https://galaxy.esn.org/section/FR/FR-LILL-ESL
the section code is 'FR-LILL-ESL'

### Step 4.2

Import this if you want to send a push when you add an events/news/parteners content 
and replace YOUR_CODE_SECTION by your own

```json
{ "rules_send_push" : {
    "LABEL" : "send push after events",
    "PLUGIN" : "reaction rule",
    "OWNER" : "rules",
    "TAGS" : [ "content", "push" ],
    "REQUIRES" : [ "rules", "rules_http_client" ],
    "ON" : {
      "node_insert--event" : { "bundle" : "event" },
      "node_insert--news" : { "bundle" : "news" },
      "node_insert--partner" : { "bundle" : "partner" }
    },
    "DO" : [
      { "request_url" : {
          "USING" : {
            "url" : "http:\/\/esnlille.fr\/survivalGuide\/includes\/rest\/sendNotification.php",
            "headers" : "Content-type: application\/x-www-form-urlencoded",
            "method" : "POST",
            "data" : "code_section=YOUR_CODE_SECTION\r\nsubject=ESN [node:content-type] \r\nmessage=[node:title]"
          },
          "PROVIDE" : { "http_response" : { "http_response" : "HTTP data" } }
        }
      }
    ]
  }
}
```

# Suggest

If you see any bugs you want to report or any suggestion to improve the app, please write an email to jeremie.samson76@gmail.com and speak your mind, I'll answer as fast as I can. 


