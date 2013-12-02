<?php
namespace ${namespaceDir};

use Doctrine\ORM\Mapping as ORM;

/**
 <#if repoDeclared>
 * @ORM\Entity(repositoryClass="${namespaceForRepository}\${name}Repository")
<#else>
 * @ORM\Entity
</#if>
 * @ORM\Table(name="${optionalTableName}")
 */
class ${name}
{
    /**
     * @ORM\Id
     * @ORM\Column(type="integer")
     * @ORM\GeneratedValue(strategy="AUTO")
     */
    protected $id;
    
    //put your code here
}