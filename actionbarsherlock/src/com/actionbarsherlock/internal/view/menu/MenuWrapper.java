package com.actionbarsherlock.internal.view.menu;

import java.util.WeakHashMap;
import android.content.ComponentName;
import android.content.Intent;
import android.view.KeyEvent;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

public class MenuWrapper implements Menu {
    private final android.view.Menu mNativeMenu;

    private final WeakHashMap<android.view.MenuItem, MenuItem> mNativeMap =
            new WeakHashMap<android.view.MenuItem, MenuItem>();


    public MenuWrapper(android.view.Menu nativeMenu) {
        mNativeMenu = nativeMenu;
    }

    public android.view.Menu unwrap() {
        return mNativeMenu;
    }

    private MenuItem addInternal(android.view.MenuItem nativeItem) {
        MenuItem item = new MenuItemWrapper(nativeItem);
        mNativeMap.put(nativeItem, item);
        return item;
    }

    public MenuItem add(CharSequence title) {
        return addInternal(mNativeMenu.add(title));
    }

    public MenuItem add(int titleRes) {
        return addInternal(mNativeMenu.add(titleRes));
    }

    public MenuItem add(int groupId, int itemId, int order, CharSequence title) {
        return addInternal(mNativeMenu.add(groupId, itemId, order, title));
    }

    public MenuItem add(int groupId, int itemId, int order, int titleRes) {
        return addInternal(mNativeMenu.add(groupId, itemId, order, titleRes));
    }

    private SubMenu addInternal(android.view.SubMenu nativeSubMenu) {
        SubMenu subMenu = new SubMenuWrapper(nativeSubMenu);
        android.view.MenuItem nativeItem = nativeSubMenu.getItem();
        MenuItem item = subMenu.getItem();
        mNativeMap.put(nativeItem, item);
        return subMenu;
    }

    public SubMenu addSubMenu(CharSequence title) {
        return addInternal(mNativeMenu.addSubMenu(title));
    }

    public SubMenu addSubMenu(int titleRes) {
        return addInternal(mNativeMenu.addSubMenu(titleRes));
    }

    public SubMenu addSubMenu(int groupId, int itemId, int order, CharSequence title) {
        return addInternal(mNativeMenu.addSubMenu(groupId, itemId, order, title));
    }

    public SubMenu addSubMenu(int groupId, int itemId, int order, int titleRes) {
        return addInternal(mNativeMenu.addSubMenu(groupId, itemId, order, titleRes));
    }

    public int addIntentOptions(int groupId, int itemId, int order, ComponentName caller, Intent[] specifics, Intent intent, int flags, MenuItem[] outSpecificItems) {
        android.view.MenuItem[] nativeOutItems = new android.view.MenuItem[outSpecificItems.length];
        int result = mNativeMenu.addIntentOptions(groupId, itemId, order, caller, specifics, intent, flags, nativeOutItems);
        for (int i = 0, length = outSpecificItems.length; i < length; i++) {
            outSpecificItems[i] = new MenuItemWrapper(nativeOutItems[i]);
        }
        return result;
    }

    public void removeItem(int id) {
        mNativeMenu.removeItem(id);
    }

    public void removeGroup(int groupId) {
        mNativeMenu.removeGroup(groupId);
    }

    public void clear() {
        mNativeMap.clear();
        mNativeMenu.clear();
    }

    public void setGroupCheckable(int group, boolean checkable, boolean exclusive) {
        mNativeMenu.setGroupCheckable(group, checkable, exclusive);
    }

    public void setGroupVisible(int group, boolean visible) {
        mNativeMenu.setGroupVisible(group, visible);
    }

    public void setGroupEnabled(int group, boolean enabled) {
        mNativeMenu.setGroupEnabled(group, enabled);
    }

    public boolean hasVisibleItems() {
        return mNativeMenu.hasVisibleItems();
    }

    public MenuItem findItem(int id) {
        android.view.MenuItem nativeItem = mNativeMenu.findItem(id);
        return findItem(nativeItem);
    }

    public MenuItem findItem(android.view.MenuItem nativeItem) {
        if (nativeItem == null) {
            return null;
        }

        MenuItem wrapped = mNativeMap.get(nativeItem);
        if (wrapped != null) {
            return wrapped;
        }

        return addInternal(nativeItem);
    }

    public int size() {
        return mNativeMenu.size();
    }

    public MenuItem getItem(int index) {
        android.view.MenuItem nativeItem = mNativeMenu.getItem(index);
        return findItem(nativeItem);
    }

    public void close() {
        mNativeMenu.close();
    }

    public boolean performShortcut(int keyCode, KeyEvent event, int flags) {
        return mNativeMenu.performShortcut(keyCode, event, flags);
    }

    public boolean isShortcutKey(int keyCode, KeyEvent event) {
        return mNativeMenu.isShortcutKey(keyCode, event);
    }

    public boolean performIdentifierAction(int id, int flags) {
        return mNativeMenu.performIdentifierAction(id, flags);
    }

    public void setQwertyMode(boolean isQwerty) {
        mNativeMenu.setQwertyMode(isQwerty);
    }
}
