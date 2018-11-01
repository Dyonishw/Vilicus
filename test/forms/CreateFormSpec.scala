package forms

import forms.CreateForm.ListItemWrite
import org.scalatestplus.play.PlaySpec
import play.api.mvc.Request
import play.api.test.FakeRequest

class CreateFormSpec extends PlaySpec {


  "Testing the CreateForm " should {

    "should work with bindFromRequest" in {

      val call = controllers.routes.CreateController.createItem()
      implicit val request: Request[_] = FakeRequest(call)
        .withFormUrlEncodedBody(
          "id" -> "9000",
          "itemType" -> "Oil",
          "itemSubType" -> "FishOil",
          "brand" -> "FishOilBrand",
          "SKU" -> "Pack",
          "quantity" -> "0"
        )

      val boundForm = CreateForm.form.bindFromRequest()
      val formData = boundForm.value.get

      formData.id mustBe 9000
      formData.itemType mustBe "Oil"
      formData mustBe a [ListItemWrite]

    }

    "should work with raw data" in {

      val rawData = Map(
        "id" -> "9000",
        "itemType" -> "Oil",
        "itemSubType" -> "FishOil",
        "brand" -> "FishOilBrand",
        "SKU" -> "Pack",
        "quantity" -> "0"
      )

      val rawForm = CreateForm.form.bind(rawData)
      val formData = rawForm.value.get

      formData.id mustBe 9000
      formData.itemType mustBe "Oil"

    }

    "should fail with incorrect data" in {

        val call = controllers.routes.CreateController.createItem()
        implicit val request: Request[_] = FakeRequest(call)
          .withFormUrlEncodedBody(
            "id" -> "9000",
            "itemType" -> "Oil",
            "itemSubType" -> "FishOil",
            "brand" -> "FishOilBrand",
            "SKU" -> "Pack",
            "quantity" -> "-1"
          )

        val boundForm = CreateForm.form.bindFromRequest()
        val errorsInForm = boundForm.errors.head

        errorsInForm.key mustBe "quantity"
        errorsInForm.message mustBe "error.min"
        boundForm.hasGlobalErrors mustBe false
        boundForm.hasErrors mustBe true

    }
  }
}
