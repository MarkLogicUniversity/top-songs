xquery version "1.0-ml";
(: 
Copyright 2002-2012 MarkLogic Corporation.  All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
:)

module namespace extsuggest = "http://marklogic.com/rest-api/resource/suggestions";

import module namespace config-query = "http://marklogic.com/rest-api/models/config-query" at "/MarkLogic/rest-api/models/config-query-model.xqy";
import module namespace search = "http://marklogic.com/appservices/search" at "/MarkLogic/appservices/search/search.xqy";
declare namespace tsr = "http://marklogic.com/MLU/top-songs/resources";

declare default function namespace "http://www.w3.org/2005/xpath-functions";
declare option xdmp:mapping "false";



declare function extsuggest:is-quoted($arg as xs:string) {
    let $arg := $arg
    return (fn:starts-with($arg,'"') and fn:ends-with($arg,'"'))
};

declare function extsuggest:get(
    $context as map:map,
    $params  as map:map
) as document-node()*
{
    let $output-types := map:put($context,"output-types","application/xml")
    let $q := map:get($params,"q")
    let $options := config-query:get-options("suggestion-options")
    let $_ := xdmp:log(fn:concat(" search text is ",$q),"info")
    let $content :=
        if (exists($q))
        then search:suggest(fn:concat("*",$q,"*"), $options)
        else ()
    let $_ := xdmp:log(fn:concat(" search suggestions are ",$content),"info")
    return (xdmp:set-response-code(200,"OK"), 
               document { element Suggestions { for $i in $content 
                                                return element suggestion {if (extsuggest:is-quoted($i) ) then (fn:substring($i,2,fn:string-length($i)-2) ) else ($i) } } } 
            )
};
