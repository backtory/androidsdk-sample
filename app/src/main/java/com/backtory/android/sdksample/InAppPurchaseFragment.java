package com.backtory.android.sdksample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.backtory.android.iap.IapResult;
import com.backtory.android.iap.Purchase;
import com.backtory.android.iap.SkuDetails;
import com.backtory.java.internal.BacktoryIap;
import com.backtory.java.internal.BacktoryIapListener;

import java.util.ArrayList;


/**
 * Created by mohammad on 2/15/17.
 *
 */
public class InAppPurchaseFragment extends MainActivity.AbsFragment implements BacktoryIapListener {

    public BacktoryIap backtoryIAP;

    private Spinner itemTypeSpinner;
    private Spinner securityTypeSpinner;
    private final String gasSku = "gas";
    private final String infiniteGasSku = "infinite_gas";
    private final String premiumSku = "premium";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        backtoryIAP = new BacktoryIap(getContext(), this);
        itemTypeSpinner = initSimpleSpinner(v, R.id.iap_item_types, R.array.iap_item_types);
        securityTypeSpinner = initSimpleSpinner(v, R.id.iap_security_types, R.array.iap_security_types);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        backtoryIAP.handleActivityResult(requestCode, resultCode, data);
    }

    private Spinner initSimpleSpinner(View parent, int spinnerId, int strArrayId) {
        Spinner spinner = parent.findViewById(spinnerId);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                strArrayId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return spinner;
    }

    @Override
    public void onDestroy() {
        if (backtoryIAP != null)
            backtoryIAP.dispose();
        super.onDestroy();
    }

    void getSkuDetails() {

        ArrayList<String> skuList = new ArrayList<>();
        skuList.add(gasSku);
        skuList.add(premiumSku);
        backtoryIAP.getSkuDetailsInBackground(skuList);
    }

    void getPurchases() {
        String itemType = itemTypeSpinner.getSelectedItem().toString();
        if (itemType.equals("")) {
            textView.setText("No item type selected!");
            return;
        }

        backtoryIAP.getPurchases(itemType, null);
    }

    void purchaseItem() {
        String securityType = securityTypeSpinner.getSelectedItem().toString();

        if (securityType.equals("secure"))
            backtoryIAP.securePurchase(getActivity(), gasSku, "my webhook message");
        else
            backtoryIAP.insecurePurchase(getActivity(), gasSku);
    }

    private String sku;
    private String purchaseToken;

    void consumeItem() {
        if (purchaseToken == null) {
            textView.setText("No purchaseToken is available!");
            return;
        }

        String securityType = securityTypeSpinner.getSelectedItem().toString();

        if (securityType.equals("secure"))
            backtoryIAP.secureConsume(sku, purchaseToken, "my webhook message");
        else
            backtoryIAP.insecureConsume(sku, purchaseToken);
    }

    void upgradeToPremium() {
        String securityType = securityTypeSpinner.getSelectedItem().toString();

        if (securityType.equals("secure"))
            backtoryIAP.securePurchase(getActivity(), premiumSku, "my webhook message");
        else
            backtoryIAP.insecurePurchase(getActivity(), premiumSku);
    }

    void subscribe() {
        String securityType = securityTypeSpinner.getSelectedItem().toString();

        if (securityType.equals("secure"))
            backtoryIAP.secureSubscribe(getActivity(), infiniteGasSku, "my webhook message");
        else
            backtoryIAP.insecureSubscribe(getActivity(), infiniteGasSku);
    }

    @Override
    protected int[] getButtonsId() {
        return new int[]{R.id.get_sku_details, R.id.get_purchases,
                R.id.purchase_item, R.id.consume_item,
                R.id.upgrade_to_premium, R.id.subscribe};
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_iap;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_sku_details:
                getSkuDetails();
                break;
            case R.id.get_purchases:
                getPurchases();
                break;
            case R.id.purchase_item:
                purchaseItem();
                break;
            case R.id.consume_item:
                consumeItem();
                break;
            case R.id.upgrade_to_premium:
                upgradeToPremium();
                break;
            case R.id.subscribe:
                subscribe();
                break;
        }
    }

    @Override
    public void onGetSkuDetailsFinished(IapResult result, ArrayList<SkuDetails> skuDetailsList) {
        textView.setText("Get sku details finished.\nResult: " + MainActivity.gson.toJson(result) +
                "\nSkuDetailsList: " + MainActivity.gson.toJson(skuDetailsList));
    }

    @Override
    public void onGetPurchasesFinished(IapResult result, ArrayList<String> ownedSkus, String continuationToken) {
        textView.setText("Get purchases finished.\nResult: " + MainActivity.gson.toJson(result) +
                "\nOwnedSkus: " + MainActivity.gson.toJson(ownedSkus) +
                "\nContinuationToken: " + continuationToken);
    }

    @Override
    public void onPurchaseFinished(IapResult result, Purchase purchase, String webhookMessage) {
        textView.setText("Purchase finished.\nResult: " + MainActivity.gson.toJson(result) +
                "\nPurchase: " + (purchase != null ? purchase.toJSONObject() : "null") +
                "\nWebhook message: " + webhookMessage);
        if (result.getResultCode() == IapResult.SUCCESS) {
            this.sku = purchase.getProductId();
            this.purchaseToken = purchase.getPurchaseToken();
        }
    }

    @Override
    public void onConsumptionFinished(IapResult result, String sku, String purchaseToken, String webhookMessage) {
        textView.setText("Consume finished.\nResult: " + MainActivity.gson.toJson(result) +
                "\nSku: " + sku + "\nPurchaseToken: " + purchaseToken +
                "\nWebhook message: " + webhookMessage);
        if (result.getResultCode() == IapResult.SUCCESS) {
            this.sku = null;
            this.purchaseToken = null;
        }
    }

    @Override
    public void onSubscriptionFinished(IapResult result, Purchase purchase, String webhookMessage) {
        textView.setText("Subscription finished.\nResult: " + MainActivity.gson.toJson(result) +
                "\nSubscription: " + (purchase != null ? purchase.toJSONObject() : "null") +
                "\nWebhook message: " + webhookMessage);
    }
}
