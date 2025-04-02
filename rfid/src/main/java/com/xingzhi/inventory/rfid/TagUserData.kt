package com.xingzhi.inventory.rfid

data class TagUserData(
    private var uid: String? = null,
    var afi: Byte = 0,
    var dsfID: Byte = 0,
    var primaryItemIdentifier: String? = null,
    var contentParameter: String? = null,
    var ownerInstitution: String? = null,
    var setInformation: String? = null,
    var typeOfUsage: String? = null,
    var shelfLocation: String? = null,
    var onixMediaformat: String? = null,
    var marcMediaformat: String? = null,
    var supplierIdentifier: String? = null,
    var orderNumber: String? = null,
    var illBorrowingInstitution: String? = null,
    var illBorrowingTransactionNumber: String? = null,
    var gs1ProductIdentifier: String? = null,
    var localDataA: String? = null,
    var localDataB: String? = null,
    var localDataC: String? = null,
    var title: String? = null,
    var productIdentifier: String? = null,
    var mediaFormat: String? = null,
    var supplyChainStage: String? = null,
    var supplierInvoiceNumber: String? = null,
    var alternativeItemIdentifier: String? = null,
    var alternativeOwnerLibraryIdentifier: String? = null,
    var subsidiaryOfAnOwnerLibrary: String? = null,
    var alternativeILLBorrowingInstitution: String? = null,
) {
    fun getUID() = uid

    fun setUID(uid: String?) {
        this.uid = uid
    }
}
