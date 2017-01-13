package org.bahmni.gatling.scenarios


import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import org.bahmni.gatling.Configuration
import org.bahmni.gatling.HttpRequests._
import org.bahmni.gatling.HttpRequestsEncounter._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

/**
  * Created by swarup on 12/20/16.
  */
object PatientCreateAndStartVisitFlow {


  val createPatient : ChainBuilder = exec (
    createPatientRequest
      .check(
        jsonPath("$.patient.uuid").saveAs("patient_uuid"),
        status.is(200)
      ).resources(
          getPatient("${patient_uuid}"),
          startVisitRequest("${patient_uuid}").check(status.is(201)),
          captureEncounter("${patient_uuid}").check(status.is(200))
        )
  )

  val scn : ScenarioBuilder = scenario("create Patient, start visit and capture Encounter")
    .during(Configuration.Load.DURATION){
      exec(createPatient)
	.pause(28)
    }
}
