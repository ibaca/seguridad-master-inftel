package com.actionbarsherlock.internal.view.menu;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

/** Used to carry an instance of our version of MenuItem through a native channel. */
public class MenuItemMule implements MenuItem {
    private static final String ERROR = "Cannot interact with object designed for temporary "
            + "instance passing. Make sure you using both SherlockFragmentActivity and "
            + "SherlockFragment.";


    private final com.actionbarsherlock.view.MenuItem mItem;

    public MenuItemMule(com.actionbarsherlock.view.MenuItem item) {
        mItem = item;
    }

    public com.actionbarsherlock.view.MenuItem unwrap() {
        return mItem;
    }


    public boolean collapseActionView() {
        throw new IllegalStateException(ERROR);
    }

    public boolean expandActionView() {
        throw new IllegalStateException(ERROR);
    }

    public ActionProvider getActionProvider() {
        throw new IllegalStateException(ERROR);
    }

    public View getActionView() {
        throw new IllegalStateException(ERROR);
    }

    public char getAlphabeticShortcut() {
        throw new IllegalStateException(ERROR);
    }

    public int getGroupId() {
        throw new IllegalStateException(ERROR);
    }

    public Drawable getIcon() {
        throw new IllegalStateException(ERROR);
    }

    public Intent getIntent() {
        throw new IllegalStateException(ERROR);
    }

    public int getItemId() {
        throw new IllegalStateException(ERROR);
    }

    public ContextMenuInfo getMenuInfo() {
        throw new IllegalStateException(ERROR);
    }

    public char getNumericShortcut() {
        throw new IllegalStateException(ERROR);
    }

    public int getOrder() {
        throw new IllegalStateException(ERROR);
    }

    public SubMenu getSubMenu() {
        throw new IllegalStateException(ERROR);
    }

    public CharSequence getTitle() {
        throw new IllegalStateException(ERROR);
    }

    public CharSequence getTitleCondensed() {
        return mItem.getTitleCondensed();
        //throw new IllegalStateException(ERROR);
    }

    public boolean hasSubMenu() {
        throw new IllegalStateException(ERROR);
    }

    public boolean isActionViewExpanded() {
        throw new IllegalStateException(ERROR);
    }

    public boolean isCheckable() {
        throw new IllegalStateException(ERROR);
    }

    public boolean isChecked() {
        throw new IllegalStateException(ERROR);
    }

    public boolean isEnabled() {
        throw new IllegalStateException(ERROR);
    }

    public boolean isVisible() {
        throw new IllegalStateException(ERROR);
    }

    public MenuItem setActionProvider(ActionProvider arg0) {
        throw new IllegalStateException(ERROR);
    }

    public MenuItem setActionView(View arg0) {
        throw new IllegalStateException(ERROR);
    }

    public MenuItem setActionView(int arg0) {
        throw new IllegalStateException(ERROR);
    }

    public MenuItem setAlphabeticShortcut(char arg0) {
        throw new IllegalStateException(ERROR);
    }

    public MenuItem setCheckable(boolean arg0) {
        throw new IllegalStateException(ERROR);
    }

    public MenuItem setChecked(boolean arg0) {
        throw new IllegalStateException(ERROR);
    }

    public MenuItem setEnabled(boolean arg0) {
        throw new IllegalStateException(ERROR);
    }

    public MenuItem setIcon(Drawable arg0) {
        throw new IllegalStateException(ERROR);
    }

    public MenuItem setIcon(int arg0) {
        throw new IllegalStateException(ERROR);
    }

    public MenuItem setIntent(Intent arg0) {
        throw new IllegalStateException(ERROR);
    }

    public MenuItem setNumericShortcut(char arg0) {
        throw new IllegalStateException(ERROR);
    }

    public MenuItem setOnActionExpandListener(OnActionExpandListener arg0) {
        throw new IllegalStateException(ERROR);
    }

    public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener arg0) {
        throw new IllegalStateException(ERROR);
    }

    public MenuItem setShortcut(char arg0, char arg1) {
        throw new IllegalStateException(ERROR);
    }

    public void setShowAsAction(int arg0) {
        throw new IllegalStateException(ERROR);
    }

    public MenuItem setShowAsActionFlags(int arg0) {
        throw new IllegalStateException(ERROR);
    }

    public MenuItem setTitle(CharSequence arg0) {
        throw new IllegalStateException(ERROR);
    }

    public MenuItem setTitle(int arg0) {
        throw new IllegalStateException(ERROR);
    }

    public MenuItem setTitleCondensed(CharSequence arg0) {
        throw new IllegalStateException(ERROR);
    }

    public MenuItem setVisible(boolean arg0) {
        throw new IllegalStateException(ERROR);
    }
}
