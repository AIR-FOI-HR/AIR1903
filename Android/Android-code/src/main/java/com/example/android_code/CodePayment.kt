import android.content.Context
import com.example.pop_sajamv2.Session
import com.example.webservice.Common.Common
import com.example.webservice.Model.Invoice
import com.example.webservice.Model.OneInvoiceResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Response

class CodePayment (private val invoiceCode: String) : com.example.core.PaymentInterface {
    var id :Int = 0
    lateinit var invoice:Invoice

    override fun pay(context: Context):OneInvoiceResponse {
        val api = Common.api

        val call: Call<OneInvoiceResponse> = api.finalizeInvoiceCode(Session.user.Token, Session.user.KorisnickoIme, true,  invoiceCode)
        lateinit var response:Response<OneInvoiceResponse>
        runBlocking {
            val crt = GlobalScope.async {
                response = call.execute()
            }
            println(crt.await())
        }
        return response.body()!!
    }
}