%dw 1.0
%output application/java
---
{
	Delimiters: "????",
	FunctionalAcksGenerated: [{
		Interchange: {
		},
		MessageHeader: {
		},
		Id: "????",
		Name: "????",
		Heading: {
		},
		Detail: {
		},
		Summary: {
		}
	}],
	FunctionalAcksReceived: [{
		Interchange: {
		},
		Errors: [{
		} as :object {
			class : "com.mulesoft.flatfile.1.2.3.lib.schema.EdifactError"
		}],
		MessageHeader: {
		},
		Id: "????",
		Name: "????",
		Heading: {
		},
		Detail: {
		},
		Summary: {
		}
	}],
	Errors: [{
		errorCode: "????",
		errorText: "????",
		fatal: true,
		segment: 1
	} as :object {
		class : "com.mulesoft.flatfile.1.2.3.lib.schema.EdifactError"
	}],
	Messages: {
		D97A: {
			ORDERS: [{
			}],
			CONTRL: [{
			}]
		}
	}
}