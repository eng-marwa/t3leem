package com.salim.ta3limes.utilities;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.RouteInfo;
import android.os.Build;

import org.minidns.DnsClient;
import org.minidns.dnsserverlookup.AbstractDnsServerLookupMechanism;
import org.minidns.dnsserverlookup.AndroidUsingExec;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class AndroidUsingLinkProperties extends AbstractDnsServerLookupMechanism {

    private final ConnectivityManager connectivityManager;

    /**
     * Setup this DNS server lookup mechanism. You need to invoke this method only once, ideally before you do your
     * first DNS lookup.
     *
     * @param context a Context instance.
     * @return the instance of the newly setup mechanism
     */
    public static AndroidUsingLinkProperties setup(Context context) {
        AndroidUsingLinkProperties androidUsingLinkProperties = new AndroidUsingLinkProperties(context);
        DnsClient.addDnsServerLookupMechanism(androidUsingLinkProperties);
        return androidUsingLinkProperties;
    }

    public AndroidUsingLinkProperties(Context context) {
        super(AndroidUsingLinkProperties.class.getSimpleName(), AndroidUsingExec.PRIORITY - 1);
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public boolean isAvailable() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private List<String> getDnsServerAddressesOfActiveNetwork() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return null;
        }

        // ConnectivityManager.getActiveNetwork() is API 23.
        Network activeNetwork = connectivityManager.getActiveNetwork();
        if (activeNetwork == null) {
            return null;
        }

        LinkProperties linkProperties = connectivityManager.getLinkProperties(activeNetwork);
        if (linkProperties == null) {
            return null;
        }

        List<InetAddress> dnsServers = linkProperties.getDnsServers();
        return toListOfStrings(dnsServers);
    }

    @Override
    @TargetApi(21)
    public List<String> getDnsServerAddresses() {
        // First, try the API 23 approach using ConnectivityManager.getActiveNetwork().
        List<String> servers = getDnsServerAddressesOfActiveNetwork();
        if (servers != null) {
            return servers;
        }

        Network[] networks = connectivityManager.getAllNetworks();
        if (networks == null) {
            return null;
        }

        servers = new ArrayList<>(networks.length * 2);
        for (Network network : networks) {
            LinkProperties linkProperties = connectivityManager.getLinkProperties(network);
            if (linkProperties == null) {
                continue;
            }

            // Prioritize the DNS servers of links which have a default route
            if (hasDefaultRoute(linkProperties)) {
                servers.addAll(0, toListOfStrings(linkProperties.getDnsServers()));
            } else {
                servers.addAll(toListOfStrings(linkProperties.getDnsServers()));
            }
        }

        if (servers.isEmpty()) {
            return null;
        }

        return servers;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static boolean hasDefaultRoute(LinkProperties linkProperties) {
        for (RouteInfo route : linkProperties.getRoutes()) {
            if (route.isDefaultRoute()) {
                return true;
            }
        }
        return false;
    }

}