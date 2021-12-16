package com.FrederikDaniel_jmartMH;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.FrederikDaniel_jmartMH.model.Account;
import com.FrederikDaniel_jmartMH.model.Invoice;
import com.FrederikDaniel_jmartMH.model.Payment;
import com.FrederikDaniel_jmartMH.model.Product;
import com.FrederikDaniel_jmartMH.request.PaymentRequest;
import com.FrederikDaniel_jmartMH.request.TopUpRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;

public class InvoiceCardView extends RecyclerView.Adapter<InvoiceCardView.InvoiceCardViewViewHolder> {
    private ArrayList<Payment> listPayment = new ArrayList<>();
    private Product product;
    private static final Gson gson = new Gson();
    private Dialog dialog;
    private Payment.Record lastRecord;
    private Account account = LoginActivity.getLoggedAccount();
    public InvoiceCardView(ArrayList<Payment> userInvoiceList) {this.listPayment = userInvoiceList;
    }

    @NonNull
    @Override
    public InvoiceCardView.InvoiceCardViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_invoice_card_view, viewGroup, false);
        return new InvoiceCardView.InvoiceCardViewViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull InvoiceCardViewViewHolder holder, int position) {
        Payment payment = listPayment.get(position);
        holder.noInvoice.setText("#" + payment.id);
        lastRecord = payment.history.get(payment.history.size() - 1);
        holder.invoiceStatus.setText(lastRecord.status.toString());
        holder.invoiceDate.setText(lastRecord.date.toString());
        holder.invoiceAddress.setText(payment.shipment.address);

        getProductData(holder, payment);
        holder.buttonReceipt.setVisibility(View.GONE);

        holder.buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> listenerAcceptPayment = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Boolean isAccepted = Boolean.valueOf(response);
                        if (isAccepted) {
                            Toast.makeText(holder.CardViewInvoice.getContext(), "PAYMENT CONFIRMED", Toast.LENGTH_SHORT).show();
                            payment.history.add(new Payment.Record(Invoice.Status.ON_PROGRESS, "PAYMENT CONFIRMED"));
                            lastRecord = payment.history.get(payment.history.size() - 1);
                            holder.invoiceStatus.setText(lastRecord.status.toString());
                            holder.layoutConfirm.setVisibility(View.GONE);
                            holder.buttonReceipt.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(holder.CardViewInvoice.getContext(), "Accept failed, Payment cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                Response.ErrorListener errorListenerAcceptPayment = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(holder.CardViewInvoice.getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                    }
                };

                PaymentRequest acceptPaymentRequest = new PaymentRequest(payment.id, listenerAcceptPayment, errorListenerAcceptPayment);
                RequestQueue queue = Volley.newRequestQueue(holder.CardViewInvoice.getContext());
                queue.add(acceptPaymentRequest);
            }
        });

        holder.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> listenerCancelPayment = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Boolean isAccepted = Boolean.valueOf(response);     //response berbentuk boolean pertanda keberhasilan untuk melakukan cancel payment
                        if (isAccepted) {
                            double price = Double.valueOf(holder.invoiceCost.getText().toString().trim().substring(3));
                            Response.Listener<String> listener = new Response.Listener<String>() {      //listener top up
                                @Override
                                public void onResponse(String response) {
                                    Boolean object = Boolean.valueOf(response);
                                    if (object) {
                                        Toast.makeText(holder.CardViewInvoice.getContext(), "Balance has been returned", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(holder.CardViewInvoice.getContext(), "Balance can't be returned", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            };

                            Response.ErrorListener errorListener = new Response.ErrorListener() {       //errorListener jika tidak terkoneksi ke backend
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(holder.CardViewInvoice.getContext(), "Failed Connection", Toast.LENGTH_SHORT).show();
                                }
                            };
                            TopUpRequest topUpRequest = new TopUpRequest(price, payment.buyerId, listener, errorListener);
                            RequestQueue queue = Volley.newRequestQueue(holder.CardViewInvoice.getContext());
                            queue.add(topUpRequest);
                            Toast.makeText(holder.CardViewInvoice.getContext(), "Payment Cancelled", Toast.LENGTH_SHORT).show();
                            payment.history.add(new Payment.Record(Invoice.Status.CANCELLED, "Payment Cancelled!"));
                            lastRecord = payment.history.get(payment.history.size() - 1);
                            holder.invoiceStatus.setText(lastRecord.status.toString());
                            holder.layoutConfirm.setVisibility(View.GONE);

                        } else {
                            Toast.makeText(holder.CardViewInvoice.getContext(), "This payment can't be cancelled!", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                Response.ErrorListener errorListenerCancelPayment = new Response.ErrorListener() {      //errorListener jika tidak terkoneksi ke backend
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(holder.CardViewInvoice.getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                    }
                };

                PaymentRequest cancelPaymentRequest = new PaymentRequest(listenerCancelPayment, payment.id, errorListenerCancelPayment);
                RequestQueue queue = Volley.newRequestQueue(holder.CardViewInvoice.getContext());
                queue.add(cancelPaymentRequest);
            }
        });

//        holder.buttonReceipt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog = new Dialog(holder.buttonAccept.getContext());
//                dialog.setContentView(R.layout.resi_dialog);
//                EditText editReceipt = dialog.findViewById(R.id.editReceipt);
//                Button buttonSubmit = dialog.findViewById(R.id.buttonSubmit);
//                dialog.show();
//                buttonSubmit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        String dataRecipt = editRecipt.getText().toString().trim();
//                        Response.Listener<String> listenerSubmitPayment = new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                Boolean isSubmitted = Boolean.valueOf(response);
//                                if (isSubmitted) {
//                                    Toast.makeText(holder.CardViewInvoice.getContext(), "Payment Submitted", Toast.LENGTH_SHORT).show();
//                                    payment.history.add(new Payment.Record(Invoice.Status.ON_DELIVERY, "Payment has been Submitted"));
//                                    lastRecord = payment.history.get(payment.history.size() - 1);
//                                    holder.invoiceStatus.setText(lastRecord.status.toString());
//                                    holder.CardViewInvoice.setVisibility(View.GONE);
//                                } else {
//                                    Toast.makeText(holder.CardViewInvoice.getContext(), "This payment can't be submitted! " + payment.id, Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        };
//
//                        Response.ErrorListener errorListenerSubmitPayment = new Response.ErrorListener() {      //errorListener jika tidak terkoneksi ke backend
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(holder.CardViewInvoice.getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
//                            }
//                        };
//                        PaymentRequest submitPaymentRequest = new PaymentRequest(payment.id, dataRecipt, listenerSubmitPayment, errorListenerSubmitPayment);
//                        RequestQueue queue = Volley.newRequestQueue(holder.CardViewInvoice.getContext());
//                        queue.add(submitPaymentRequest);
//                        dialog.dismiss();
//                    }
//                });
//            }
//        });
    }

    private void getProductData(InvoiceCardViewViewHolder holder, Payment payment) {
    }

    @Override
    public int getItemCount() {
        return listPayment.size();
    }

    class InvoiceCardViewViewHolder extends RecyclerView.ViewHolder{
        TextView noInvoice, invoiceName, invoiceStatus, invoiceDate, invoiceAddress, invoiceCost;
        ImageButton invoiceDetailButton;

        CardView CardViewInvoice;
        Button buttonAccept, buttonReceipt, buttonCancel;
        LinearLayout InvoiceDetail,layoutConfirm;
        public InvoiceCardViewViewHolder(View itemView) {
            super(itemView);
            noInvoice = itemView.findViewById(R.id.InvoiceNumber);
            invoiceDetailButton = itemView.findViewById(R.id.detailInvoiceButton);
            invoiceName = itemView.findViewById(R.id.invoiceName);
            invoiceStatus = itemView.findViewById(R.id.invoiceStatus);
            invoiceDate = itemView.findViewById(R.id.invoiceDate);
            invoiceAddress = itemView.findViewById(R.id.invoiceAddress);
            invoiceCost = itemView.findViewById(R.id.invoiceCost);
            CardViewInvoice = itemView.findViewById(R.id.InvoiceCardview);
            buttonAccept = itemView.findViewById(R.id.buttonAcceptInvoice);
            buttonReceipt = itemView.findViewById(R.id.buttonReceiptInvoice);
            buttonCancel = itemView.findViewById(R.id.buttonCancelInvoice);
            layoutConfirm = itemView.findViewById(R.id.layoutConfirmation);
            InvoiceDetail = itemView.findViewById(R.id.LinearLInvoiceDetail);
        }
        }
}
