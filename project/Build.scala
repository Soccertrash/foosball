import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "foosball"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
    	lessEntryPoints <<= (sourceDirectory in Compile)(base => (
						(base / "assets" / "stylesheets" / "bootstrap" * "bootstrap.less") +++
						(base / "assets" / "stylesheets" / "bootstrap" * "responsive.less")
							))
    	
    	    )

}