# Strava report

This clojure script uses the Strava Java API https://github.com/danshannon/javastravav3api/ to fetch
activities for a given year and produce a google chart diagram showing distances per month and disciplines.

Since I have been doing running, cross-country skiing, roller skiing, bike riding and swimming, these are the reported disciplines. But it
should be easy to customize.

## Installation

Your system needs to have a Java 8 JRE and leiningen installed. For leiningen installation instructions, see http://leiningen.org/#install.

Unfortunately Strava has made it really difficult to share third party Strava API based apps. To give this app a spin you have to go into http://strava.com/settings/api and add the application to obtain a client ID and client secret. With the client ID and client secret you can make a request towards strava.com to get a generated access code. See the example URL in the authorize request here http://strava.github.io/api/v3/oauth/ and observe the HTTP response which will include the code as a parameter. Copy client/config.clj.template to client/config.clj and insert your values for client secret, code and app-id.

## Usage

    $ lein run <year> <output html file>

## Example

An example generated with my Strava data can be seen here: http://www.bolland.nu/strava2015.html

## License

Copyright © 2015 Ola Flisbäck

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
