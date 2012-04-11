package com.actionbarsherlock.internal.view.menu;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import com.actionbarsherlock.internal.view.ActionProviderWrapper;
import com.actionbarsherlock.view.ActionProvider;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

public class MenuItemWrapper implements MenuItem, android.view.MenuItem.OnMenuItemClickListener, android.view.MenuItem.OnActionExpandListener {
    private final android.view.MenuItem mNativeItem;
    private SubMenu mSubMenu = null;
    private OnMenuItemClickListener mMenuItemClickListener = null;
    private OnActionExpandListener mActionExpandListener = null;


    public MenuItemWrapper(android.view.MenuItem nativeItem) {
        if (nativeItem == null) {
            throw new IllegalStateException("Wrapped menu item cannot be null.");
        }
        mNativeItem = nativeItem;
    }


    public int getItemId() {
        return mNativeItem.getItemId();
    }

    public int getGroupId() {
        return mNativeItem.getGroupId();
    }

    public int getOrder() {
        return mNativeItem.getOrder();
    }

    public MenuItem setTitle(CharSequence title) {
        mNativeItem.setTitle(title);
        return this;
    }

    public MenuItem setTitle(int title) {
        mNativeItem.setTitle(title);
        return this;
    }

    public CharSequence getTitle() {
        return mNativeItem.getTitle();
    }

    public MenuItem setTitleCondensed(CharSequence title) {
        mNativeItem.setTitleCondensed(title);
        return this;
    }

    public CharSequence getTitleCondensed() {
        return mNativeItem.getTitleCondensed();
    }

    public MenuItem setIcon(Drawable icon) {
        mNativeItem.setIcon(icon);
        return this;
    }

    public MenuItem setIcon(int iconRes) {
        mNativeItem.setIcon(iconRes);
        return this;
    }

    public Drawable getIcon() {
        return mNativeItem.getIcon();
    }

    public MenuItem setIntent(Intent intent) {
        mNativeItem.setIntent(intent);
        return this;
    }

    public Intent getIntent() {
        return mNativeItem.getIntent();
    }

    public MenuItem setShortcut(char numericChar, char alphaChar) {
        mNativeItem.setShortcut(numericChar, alphaChar);
        return this;
    }

    public MenuItem setNumericShortcut(char numericChar) {
        mNativeItem.setNumericShortcut(numericChar);
        return this;
    }

    public char getNumericShortcut() {
        return mNativeItem.getNumericShortcut();
    }

    public MenuItem setAlphabeticShortcut(char alphaChar) {
        mNativeItem.setAlphabeticShortcut(alphaChar);
        return this;
    }

    public char getAlphabeticShortcut() {
        return mNativeItem.getAlphabeticShortcut();
    }

    public MenuItem setCheckable(boolean checkable) {
        mNativeItem.setCheckable(checkable);
        return this;
    }

    public boolean isCheckable() {
        return mNativeItem.isCheckable();
    }

    public MenuItem setChecked(boolean checked) {
        mNativeItem.setChecked(checked);
        return this;
    }

    public boolean isChecked() {
        return mNativeItem.isChecked();
    }

    public MenuItem setVisible(boolean visible) {
        mNativeItem.setVisible(visible);
        return this;
    }

    public boolean isVisible() {
        return mNativeItem.isVisible();
    }

    public MenuItem setEnabled(boolean enabled) {
        mNativeItem.setEnabled(enabled);
        return this;
    }

    public boolean isEnabled() {
        return mNativeItem.isEnabled();
    }

    public boolean hasSubMenu() {
        return mNativeItem.hasSubMenu();
    }

    public SubMenu getSubMenu() {
        if (hasSubMenu() && (mSubMenu == null)) {
            mSubMenu = new SubMenuWrapper(mNativeItem.getSubMenu());
        }
        return mSubMenu;
    }

    public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
        mMenuItemClickListener = menuItemClickListener;
        //Register ourselves as the listener to proxy
        mNativeItem.setOnMenuItemClickListener(this);
        return this;
    }

    public boolean onMenuItemClick(android.view.MenuItem item) {
        if (mMenuItemClickListener != null) {
            return mMenuItemClickListener.onMenuItemClick(this);
        }
        return false;
    }

    public ContextMenuInfo getMenuInfo() {
        return mNativeItem.getMenuInfo();
    }

    public void setShowAsAction(int actionEnum) {
        mNativeItem.setShowAsAction(actionEnum);
    }

    public MenuItem setShowAsActionFlags(int actionEnum) {
        mNativeItem.setShowAsActionFlags(actionEnum);
        return this;
    }

    public MenuItem setActionView(View view) {
        mNativeItem.setActionView(view);
        return this;
    }

    public MenuItem setActionView(int resId) {
        mNativeItem.setActionView(resId);
        return this;
    }

    public View getActionView() {
        return mNativeItem.getActionView();
    }

    public MenuItem setActionProvider(ActionProvider actionProvider) {
        mNativeItem.setActionProvider(new ActionProviderWrapper(actionProvider));
        return this;
    }

    public ActionProvider getActionProvider() {
        android.view.ActionProvider nativeProvider = mNativeItem.getActionProvider();
        if (nativeProvider != null && nativeProvider instanceof ActionProviderWrapper) {
            return ((ActionProviderWrapper)nativeProvider).unwrap();
        }
        return null;
    }

    public boolean expandActionView() {
        return mNativeItem.expandActionView();
    }

    public boolean collapseActionView() {
        return mNativeItem.collapseActionView();
    }

    public boolean isActionViewExpanded() {
        return mNativeItem.isActionViewExpanded();
    }

    public MenuItem setOnActionExpandListener(OnActionExpandListener listener) {
        mActionExpandListener = listener;
        //Register ourselves as the listener to proxy
        mNativeItem.setOnActionExpandListener(this);
        return this;
    }

    public boolean onMenuItemActionCollapse(android.view.MenuItem item) {
        if (mActionExpandListener != null) {
            return mActionExpandListener.onMenuItemActionCollapse(this);
        }
        return false;
    }

    public boolean onMenuItemActionExpand(android.view.MenuItem item) {
        if (mActionExpandListener != null) {
            return mActionExpandListener.onMenuItemActionExpand(this);
        }
        return false;
    }
}
