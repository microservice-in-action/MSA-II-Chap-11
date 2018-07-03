package users

import (
	"fmt"
	"github.com/ServiceComb/go-chassis/core/config"
	"os"
)

var (
	domain string
	entitymap = map[string]string{
		"customer": "customers",
		"address":  "addresses",
		"card":     "cards",
	}
)

func Init() {
	//flag.StringVar(&domain, "link-domain", os.Getenv("HATEAOS"), "HATEAOS link domain")
	globaldef := config.GlobalDefinition
	protocols := globaldef.Cse.Protocols

	if os.Getenv("front_mesher") != "" || os.Getenv("order_mesher") != "" {
		domain = "user"
	}else {
		domain = protocols["rest"].Advertise
	}
}

type Links map[string]Href

func (l *Links) AddLink(ent string, id string) {
	nl := make(Links)
	link := fmt.Sprintf("http://%v/%v/%v", domain, entitymap[ent], id)
	nl[ent] = Href{link}
	nl["self"] = Href{link}
	*l = nl
}

func (l *Links) AddAttrLink(attr string, corent string, id string) {
	link := fmt.Sprintf("http://%v/%v/%v/%v", domain, entitymap[corent], id, entitymap[attr])
	nl := *l
	nl[entitymap[attr]] = Href{link}
	*l = nl
}

func (l *Links) AddCustomer(id string) {
	l.AddLink("customer", id)
	l.AddAttrLink("address", "customer", id)
	l.AddAttrLink("card", "customer", id)
}

func (l *Links) AddAddress(id string) {
	l.AddLink("address", id)
}

func (l *Links) AddCard(id string) {
	l.AddLink("card", id)
}

type Href struct {
	Strings string `json:"href"`
}
